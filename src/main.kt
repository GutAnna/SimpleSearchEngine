package search

import java.io.File

fun findWordInList(word: String, wordsList: List<String>) {
    var result = "Not found"
    for (i in wordsList.indices) {
        if (wordsList[i] == word) {
            result = (i + 1).toString()
            break
        }
    }
    println(result)
}

fun findSubstringInText(substr: String, text: List<String>) {
    val regex = substr.lowercase().toRegex()
    var flag = false
    for (i in text) {
        if (regex.find(i.lowercase()) != null) {
            println(i)
            flag = true
        }
    }
    if (!flag) println("No matching people found.")
}

fun printMenu() {
    println("=== Menu ===")
    println("1. Find a person")
    println("2. Print all people")
    println("0. Exit")
}

class PersonList {
    companion object {
        private var PersonMap = mutableMapOf<String, List<Int>>()
        lateinit var PersonList: List<String>

        fun initMap(text: List<String>) {
            PersonList = text
            for (i in text.indices) {
                val record = text[i].lowercase().split(" ").toList()
                for (word in record) {
                    PersonMap[word] = PersonMap.getOrDefault(word, emptyList()) + i //result.getValue(word).toList() + i
                }
            }
        }

        fun findPersonInMap(substr: String, strategy: String) {
            val wordList = substr.lowercase().split(" ").toList()
            lateinit var result: List<Int>

            when (strategy) {
                "ALL" -> {
                    result = (0 until PersonMap.size).toList()
                    for (word in wordList) {
                        if (word !in PersonMap) { result = emptyList<Int>(); break }
                        result = result.intersect(PersonMap.getValue(word)).toList()
                    }
                }
                "ANY" -> {
                    result = emptyList<Int>()
                    for (word in wordList) {
                        if (word !in PersonMap) continue
                        result = result.union(PersonMap.getValue(word)).toList()
                    }
                }
                "NONE" -> {
                    result = emptyList<Int>()
                    for (word in wordList) {
                        if (word !in PersonMap) continue
                        result = result.union(PersonMap.getValue(word)).toList()
                    }
                    val noneResult = (0 until PersonList.size).toMutableList()
                    noneResult.removeAll(result)
                    result = noneResult
                }
                else -> {
                    println("Unknown strategy")
                    return
                }
            }

            for (i in result) {
                println(PersonList[i])
            }

            if (result.isEmpty()) println("No matching people found.")
        }
    }
}


fun main(args: Array<String>) {
    val filename = args[1]
    val file = File(filename)
    var text = listOf<String>()
    if (file.exists()) {
        text = file.readLines()
    }

    PersonList.initMap(text)

    while(true) {
        printMenu()
        when(readLine()!!.toInt()) {
            1 -> {
                println("Select a matching strategy: ALL, ANY, NONE")
                val strategy = readLine()!!
                println("Enter a name or email to search all suitable people.")
                val word = readLine()!!

                PersonList.findPersonInMap(word, strategy)
            }
            2 -> { println("=== List of people ===")
                for (people in text) {
                    println(people)
                }
            }
            0 -> return
            else -> print("Incorrect option! Try again.")
        }
    }


}
