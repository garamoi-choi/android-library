package com.github.hemoptysisheart.sample.domain

class Maze(
    val width: Int,
    val height: Int
) {
    companion object {
        const val WIDTH_DEFAULT = 7
        const val WIDTH_MIN = 3

        const val HEIGHT_DEFAULT = 13
        const val HEIGHT_MIN = 3
    }

    private val _cells = Array(width * height) { index ->
        Cell(index % width, index / width)
    }
    val cells: List<Cell>
        get() = _cells.toList()

    private val _links = mutableListOf<Link>()
    val links: List<Link>
        get() = _links.toList()

    val start: Cell
    val end: Cell

    init {
        require(width >= WIDTH_MIN) { "width($width) < WIDTH_MIN($WIDTH_MIN)" }
        require(height >= HEIGHT_MIN) { "height($height) < HEIGHT_MIN($HEIGHT_MIN)" }

        val remainCells = _cells.toMutableSet()
        start = _cells[0, 0]
        end = _cells[width - 1, height - 1]
        remainCells.remove(start)
        remainCells.remove(end)

        val groups = mutableListOf(Group(start), Group(end))
        while (remainCells.isNotEmpty()) {
            val cell = remainCells.random()
            val neighborCells = neighbors(cell)
                .filter { null != it.group }

            if (neighborCells.isEmpty()) {
                // 주변에 그룹이 없다면 새 그룹을 만든다.
                groups.add(Group(cell))
            } else for (neighbor in neighborCells) {
                // 주변 셀이 그룹에 속해 있다면 주변의 모든 그룹을 하나로 합친다.
                val g1 = neighbor.group // g1은 null이 아님을 neighborCells가 보장한다.
                val g2 = cell.group
                if (g1 == g2) {
                    continue
                }

                val merged = neighbor.group!!.cells +
                        (cell.group?.cells ?: emptySet()) +
                        setOf(cell)
                val g3 = Group(*merged.toTypedArray())

                groups.remove(g1)
                groups.remove(g2)
                groups.add(g3)

                // 이웃으로 으로 링크를 추가해서 그룹이 하나로 이어지도록 한다.
                _links.add(Link(cell, neighbor))
            }
            remainCells.remove(cell)
        }
        _links.sort()
    }

    private fun neighbors(cell: Cell) = listOf(
        cell.x - 1 to cell.y,
        cell.x + 1 to cell.y,
        cell.x to cell.y - 1,
        cell.x to cell.y + 1
    ).mapNotNull { (x, y) ->
        if (x in 0 until width && y in 0 until height) {
            _cells[x, y]
        } else {
            null
        }
    }

    /**
     * 좌표값으로 배열 요소를 반환한다.
     */
    private operator fun <E> Array<E>.get(x: Int, y: Int) = _cells[index(x, y)]

    /**
     * 좌표값으로 배열 인덱스를 계산한다.
     */
    private fun index(x: Int, y: Int) = y * width + x

    fun links(cell: Cell) = _links.filter { it.contains(cell) }

    override fun toString() = listOf(
        "width=$width",
        "height=$height",
        "start=$start",
        "end=$end",
        "cells=$cells",
        "links=$links"
    ).joinToString(", ", "Maze(", ")")
}
