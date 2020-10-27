package com.example.black_horse_onboarding.view_model

data class ArticleItem(
    val type: Int,
    val title: String,
    val id: Int,
    val description: String,
    val avatar: String
) {
    companion object {
        const val TYPE_ITEM: Int = 0
        const val TYPE_HEADER: Int = 1
    }
}