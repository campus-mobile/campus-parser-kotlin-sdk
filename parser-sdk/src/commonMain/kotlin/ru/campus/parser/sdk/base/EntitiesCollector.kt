/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.base

import ru.campus.parser.sdk.model.Entity

interface EntitiesCollector {
    suspend fun collectEntities(): List<Entity>
}
