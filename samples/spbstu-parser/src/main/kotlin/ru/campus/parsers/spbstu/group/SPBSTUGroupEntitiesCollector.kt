/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.group

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import ru.campus.parser.sdk.base.EntitiesCollector
import ru.campus.parser.sdk.model.Entity
import ru.campus.parsers.spbstu.model.SpbstuFaculty

/**
 * Коллектор групп Санкт-Петербургского политехнического университета.
 * Собираем список элементов факультета с начальной страницы, формируем ссылки на каждый, делаем запросы, собираем списки
 * групп с каждого. Генерируем ссылку запроса к апи для каждого факультета, получаем в ответе json, разбираем его,
 * получем список групп.
 * */
class SPBSTUGroupEntitiesCollector(
    private val httpClient: HttpClient,
) : EntitiesCollector {
    private val baseUrl: String = "https://ruz.spbstu.ru"
    private val apiUrl: String = "https://ruz.spbstu.ru/api/v1/ruz"

    override suspend fun collectEntities(): List<Entity> {
        val page: String = httpClient.get(baseUrl)
        val document: Document = Jsoup.parse(page)
        val facultiesElements: Elements = document.select("a[class=faculty-list__link]")
        return facultiesElements.flatMap { facultyElement ->
            val facultyCode: String = facultyElement.attr("href").substringAfter("faculty/").substringBefore("/groups")
            val facultyUrl = "$apiUrl/faculties/$facultyCode/groups"
            val facultyName: String = facultyElement.ownText()
            val facultyBody: String = httpClient.get(facultyUrl)
            val json: SpbstuFaculty = Json.decodeFromString(facultyBody)
            json.groups.map { groupObject ->
                val groupName: String = groupObject.groupName
                val code: String = groupObject.code.toString()
                val course: Int = groupObject.course
                val educationForm: String = groupObject.educationForm
                val degree: Int = groupObject.degree
                val url = "$baseUrl/faculty/$facultyCode/groups/$code"
                Entity(
                    name = groupName,
                    code = code,
                    scheduleUrl = url,
                    type = Entity.Type.Group,
                    extra = Entity.Extra(
                        faculty = facultyName,
                        course = course,
                        degree = getDegree(degree),
                        educationForm = getEducationForm(educationForm)
                    )
                )
            }
        }
    }

    private fun getDegree(degree: Int): String {
        return when (degree) {
            0 -> "Бакалавр"
            1 -> "Магистр"
            2 -> "Специалист"
            3 -> "Аспирант"
            6 -> "СПО"
            else -> throw IllegalArgumentException("Can't find degree for entity $degree")
        }
    }

    private fun getEducationForm(educationForm: String): String {
        return when (educationForm) {
            "common" -> "Очная"
            "evening" -> "Очно-заочная"
            "distance" -> "Заочная"
            else -> throw IllegalArgumentException("Can't find educationForm for entity $educationForm")
        }
    }
}
