package com.example.black_horse_onboarding.view_model

data class ArticleItem(
    public val type: Int,
    public val title: String,
    public val id: Int,
    public val description: String
) {
    companion object {
        const val TYPE_ITEM: Int = 0
        const val TYPE_HEADER: Int = 1
    }
}