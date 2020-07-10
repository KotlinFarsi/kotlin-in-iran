package com.kotlinfarsi.kotlininiran

import com.beust.klaxon.Json

data class Category(
        val name: String,
        val repos: List<Repo>
)

data class Repo(
        val address: String,
        val description: String,
        val website: String
)

data class Dev(
        val address: String,
        val website: String,
        val twitter: String
)

data class Article(
        val title: String,
        val description: String,
        val link: String
)

data class Content(
        val categories: List<Category>,
        val devs: List<Dev>,
        val articles: List<Article>
)