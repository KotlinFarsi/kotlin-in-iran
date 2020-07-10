package com.kotlinfarsi.kotlininiran

import com.beust.klaxon.Klaxon
import com.google.common.io.Resources
import java.io.File


fun main() {

    val jsonFile = File(Resources.getResource("data.json").path)

    val content: Content = Klaxon().parse(jsonFile)!!

    val reposContent = getReposContent(content.categories)
    val devsContent = getDevsContent(content.devs)
    val articlesContent = getArticlesContent(content.articles)

    val finalMarkdown = File(Resources.getResource("template.md").path)
            .readText()
            .replace("{{repos}}", reposContent)
            .replace("{{devs}}", devsContent)
            .replace("{{articles}}", articlesContent)

    File(Resources.getResource("README.md").path).writeText(finalMarkdown)

}

fun getReposContent(categories: List<Category>) =
        categories.joinToString("\n\n") { category ->
            """
                ${category.name}
                
                | Name | Description | 🌍 |
                | --- | --- | --- |
                ${category.repos
                    .sortedBy { it.address.substring(it.address.indexOf("/") + 1) }
                    .joinToString("") { repo ->

                        val login = repo.address.substring(0, repo.address.indexOf("/"))
                        val loginHyper = "[@$login](https://github.com/$login)"
                        val repoName = repo.address.substring(repo.address.indexOf("/") + 1)
                        val repoHyper = "[**$repoName**](https://github.com/${repo.address})"
                        val websiteLink = repo.website.ifNotBlank { "[:arrow_upper_right:](${repo.website})" }

                        """| $loginHyper/$repoHyper | ${repo.description} | $websiteLink |
                """
                    }}""".trimIndent()
        }

fun getDevsContent(devs: List<Dev>) = """
            | Name | Twitter | 🌍 |
            | --- | --- | --- |
            ${devs
        .sortedBy { it.address }
        .joinToString("") { dev ->
            val addressHyper = "[@${dev.address}](https://github.com/${dev.address})"
            val twitterLink = dev.twitter.ifNotBlank { "[@${dev.twitter}](https://twitter.com/${dev.twitter})" }
            val websiteLink = dev.website.ifNotBlank { "[:arrow_upper_right:](${dev.website})" }

            """| $addressHyper | $twitterLink | $websiteLink |
            """
        }}""".trimIndent()

fun getArticlesContent(articles: List<Article>) = """
            | Article | Description |
            | --- | --- |
            ${articles
        .sortedBy { it.title }
        .joinToString("") { article ->
            val articleHyper = "[${article.title}](${article.link})"

            """| $articleHyper | ${article.description} |
            """
        }}""".trimIndent()

inline fun String.ifNotBlank(defaultValue: () -> String): String =
        if (isNotBlank()) defaultValue() else ""
