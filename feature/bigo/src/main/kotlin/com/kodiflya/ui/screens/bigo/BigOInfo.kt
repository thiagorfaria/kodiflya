package com.kodiflya.ui.screens.bigo

import com.kodiflya.core.plugin.BigO

data class BigOInfo(
    val bigO: BigO,
    val name: String,
    val description: String,
    val examples: List<String>,
    val analogy: String,
    val tier: String,
)

val bigOEntries: List<BigOInfo> = listOf(
    BigOInfo(
        bigO = BigO.O_1,
        name = "Constant",
        description = "No matter how big the input gets, the work stays the same.",
        examples = listOf("Hash lookup", "Array index access"),
        analogy = "Opening the first page of a book.",
        tier = "BEST",
    ),
    BigOInfo(
        bigO = BigO.O_LOG_N,
        name = "Logarithmic",
        description = "Each step halves the remaining work. Fast even on large inputs.",
        examples = listOf("Binary Search"),
        analogy = "Finding a word in a dictionary by splitting it in half each time.",
        tier = "FAST",
    ),
    BigOInfo(
        bigO = BigO.O_H,
        name = "Height",
        description = "Scales with tree height. O(log n) when balanced, O(n) when skewed.",
        examples = listOf("BST Inorder", "BST Preorder", "BST Postorder"),
        analogy = "Climbing a tree — how long depends on how tall it grew.",
        tier = "VARIES",
    ),
    BigOInfo(
        bigO = BigO.O_N,
        name = "Linear",
        description = "Runtime grows proportionally with input size. Common and acceptable.",
        examples = listOf("Linear Search", "BFS", "DFS"),
        analogy = "Reading every page of a book to find a word.",
        tier = "GOOD",
    ),
    BigOInfo(
        bigO = BigO.O_V,
        name = "Linear (vertices)",
        description = "Scales with the number of vertices in the graph.",
        examples = listOf("BFS space", "DFS space"),
        analogy = "Visiting every city on a map exactly once.",
        tier = "GOOD",
    ),
    BigOInfo(
        bigO = BigO.O_V_PLUS_E,
        name = "Linear (V+E)",
        description = "Scales with the total count of vertices plus edges — linear across the graph.",
        examples = listOf("BFS", "DFS"),
        analogy = "Counting every road and every city on a map.",
        tier = "GOOD",
    ),
    BigOInfo(
        bigO = BigO.O_N_LOG_N,
        name = "Linearithmic",
        description = "The optimal bound for comparison-based sorting. Divide and conquer wins here.",
        examples = listOf("Merge Sort", "Quick Sort (avg)"),
        analogy = "Sorting a deck of cards by repeatedly splitting the pile.",
        tier = "FAIR",
    ),
    BigOInfo(
        bigO = BigO.O_V_PLUS_E_LOG_V,
        name = "Dijkstra",
        description = "Graph traversal with a priority queue. Efficient but not cheap.",
        examples = listOf("Dijkstra's shortest path"),
        analogy = "Finding the fastest route by always trying the closest unvisited city.",
        tier = "FAIR",
    ),
    BigOInfo(
        bigO = BigO.O_N_SQUARED,
        name = "Quadratic",
        description = "Nested loops. Doubling input means 4× the work. Avoid on large datasets.",
        examples = listOf("Bubble Sort", "Insertion Sort (worst)"),
        analogy = "Comparing every pair of people in a room for a handshake.",
        tier = "SLOW",
    ),
    BigOInfo(
        bigO = BigO.O_2_N,
        name = "Exponential",
        description = "Each additional element doubles the work. Grows impossibly fast.",
        examples = listOf("Brute-force subsets", "Naive Fibonacci"),
        analogy = "Each new question doubles the number of answers you need to check.",
        tier = "BAD",
    ),
    BigOInfo(
        bigO = BigO.O_N_FACTORIAL,
        name = "Factorial",
        description = "Every permutation is explored. Practical only for the tiniest inputs.",
        examples = listOf("Brute-force TSP", "Permutation generation"),
        analogy = "Trying every possible seating arrangement at a dinner party.",
        tier = "TERRIBLE",
    ),
)
