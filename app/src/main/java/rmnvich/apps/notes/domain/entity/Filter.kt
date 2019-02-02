package rmnvich.apps.notes.domain.entity

data class Filter(
        var colors: List<Int>,
        var tags: List<Int>,
        var isUnionConditions: Boolean,
        var isOnlyWithPicture: Boolean
)