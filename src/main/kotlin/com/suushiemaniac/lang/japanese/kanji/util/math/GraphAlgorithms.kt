package com.suushiemaniac.lang.japanese.kanji.util.math

import com.suushiemaniac.lang.japanese.kanji.util.math.CollectionAlgorithms.powerset

object GraphAlgorithms {
    fun <T> Map<T, Set<T>>.topologicalSort(): List<T>? {
        // remove reflexive self-dependencies
        val noReflexive = this.mapValues { it.value - it.key }

        // explicitly add empty sets (no dependencies) for all items that only occur as passive dependencies
        val extraItemsInDeps = noReflexive.values.flatten() - noReflexive.keys
        val extrasWithEmptyDeps = extraItemsInDeps.associateWith { emptySet<T>() }

        // compile graph search data
        val data = noReflexive + extrasWithEmptyDeps

        return consumeByGroups(data, emptyList())
    }

    private tailrec fun <T> consumeByGroups(graph: Map<T, Set<T>>, accu: List<T>): List<T>? {
        val freeDepGroups = graph.filterValues { it.isEmpty() }

        if (freeDepGroups.isEmpty()) {
            return accu.takeUnless { graph.isNotEmpty() }
        }

        val noRemainingDependencies = freeDepGroups.keys
        val nextAccu = accu + noRemainingDependencies

        val remainingEntries = graph - noRemainingDependencies
        val remainingWithSortDeps = remainingEntries.mapValues { it.value - noRemainingDependencies }

        return consumeByGroups(remainingWithSortDeps, nextAccu)
    }

    fun <T> Map<T, Set<T>>.minimalIncrementalInitialSCC(): Pair<List<T>, List<T>>? {
        val keyOrder = keys.toList()

        return minimalIncrementalInitialSCC(this, keyOrder)
                ?.let { it.first to (keyOrder - it.second) }
    }

    private tailrec fun <T> minimalIncrementalInitialSCC(deps: Map<T, Set<T>>, accuKeys: List<T>): Pair<List<T>, List<T>>? {
        val attemptedSort = topoWithWhitelist(deps, accuKeys.toSet())

        if (attemptedSort != null || accuKeys.isEmpty()) {
            return attemptedSort?.let { it to accuKeys }
        }

        return minimalIncrementalInitialSCC(deps, accuKeys.drop(1))
    }

    private fun <T> topoWithBlacklist(deps: Map<T, Set<T>>, blacklist: Set<T>) =
            topoExcept(deps, blacklist, false)

    private fun <T> topoWithWhitelist(deps: Map<T, Set<T>>, whitelist: Set<T>) =
            topoExcept(deps, whitelist, true)

    private fun <T> topoExcept(deps: Map<T, Set<T>>, specialKeys: Set<T>, takeIfListed: Boolean): List<T>? {
        val cleanedDeps = deps.mapValues {
            if (it.key in specialKeys == takeIfListed) it.value else emptySet()
        }

        return cleanedDeps.topologicalSort()
    }

    fun <T> Map<T, Set<T>>.minimalInitialSCC(): Pair<List<T>, Set<T>>? {
        val keyOrder = keys.toList()

        // FIXME ideally, we should be able to compute pow **up to** len 10
        if (keyOrder.size > 10) {
            return null // failsafe (otherwise pow gets too large)
        }

        val powerset = keyOrder.powerset()

        val seqWithDrops = powerset.sortedBy { it.size }
                .asSequence()
                .mapNotNull { topoWithBlacklist(this, it)?.let { topo -> topo to it } }

        return seqWithDrops.firstOrNull()
    }
}
