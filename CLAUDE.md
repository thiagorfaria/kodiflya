# Kodiflya — Source of Truth

Kodiflya is a native Android app that visualizes algorithms for interview preparation. Algorithms animate in real time with live metrics so users can *feel* the difference between O(n²) and O(n log n).

---

## Agreed Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Kotlin | 2.0+ |
| UI | Jetpack Compose + Material Icons Extended | BOM latest stable |
| DI | Hilt | 2.51+ |
| Async | Coroutines + Flow | kotlinx-coroutines 1.8+ |
| Architecture | MVVM + Clean Architecture | — |
| State | ViewModel + StateFlow | — |
| Testing | JUnit5 + MockK + Compose UI Testing | — |
| Build | Gradle Kotlin DSL, AGP | 8.x |
| Min SDK | Android 8.0 | API 26 |
| Target SDK | — | API 35 |

**Deferred (not in v1):** Room, Retrofit, any network or persistence layer. v1 is fully in-memory.

---

## Three-Layer Plugin Engine Contract

```
┌─────────────────────────────────────────────────────────┐
│  LOGIC LAYER                                            │
│  AlgorithmPlugin interface                              │
│  Each algorithm: one file, plain Kotlin, zero UI deps   │
│  Produces: Sequence<VisualizationStep>                  │
│  RULE: zero import of android.* or androidx.*           │
└─────────────────────┬───────────────────────────────────┘
                      │ plugin.steps()
┌─────────────────────▼───────────────────────────────────┐
│  ENGINE LAYER                                           │
│  PlaybackEngine                                         │
│  Controls: speed, play/pause/reset                      │
│  Emits: StateFlow<VisualizationState>                   │
│  RULE: knows AlgorithmPlugin interface only             │
│         no knowledge of BubbleSort, BFS, etc.           │
└─────────────────────┬───────────────────────────────────┘
                      │ StateFlow<VisualizationState>
┌─────────────────────▼───────────────────────────────────┐
│  UI LAYER                                               │
│  Composables render VisualizationState                  │
│  RULE: no switch/when on algorithm names                │
│         no direct instantiation of algorithm classes    │
└─────────────────────────────────────────────────────────┘
```

**Adding a new algorithm = one new file that implements `AlgorithmPlugin`. No changes to Engine or UI.**

---

## Layer Boundary Rules

| Direction | Rule | Reason |
|---|---|---|
| Logic → Engine | ALLOWED | Engine calls `plugin.steps()` via interface |
| Engine → UI | ALLOWED | Via `StateFlow<VisualizationState>` |
| Logic → UI | **FORBIDDEN** | No compose imports in logic classes |
| UI → Logic (direct) | **FORBIDDEN** | UI never instantiates algorithm classes |
| Engine → specific algorithm class | **FORBIDDEN** | Engine only knows `AlgorithmPlugin` interface |
| ViewModel → specific algorithm | **FORBIDDEN** | ViewModel receives `AlgorithmPlugin` via Hilt injection |

**Registry pattern:** Hilt provides `Set<@JvmSuppressWildcards AlgorithmPlugin>` via multibinding. ViewModel filters the set by `Category`; it never imports individual algorithm classes.

---

## v1 Scope — Committed Algorithms

### Sorting (bar-chart visualizer)
- Bubble Sort
- Insertion Sort
- Merge Sort
- Quick Sort

### Graph — Grid Pathfinding (9×9 grid visualizer)
- BFS (Breadth-First Search)
- DFS (Depth-First Search)
- Dijkstra

### Trees (canvas tree visualizer)
- BST traversal: Inorder (L→N→R)
- BST traversal: Preorder (N→L→R)
- BST traversal: Postorder (L→R→N)

### Per-screen controls (all screens)
- Live metrics panel (3 cards: algorithm-specific counters)
- Speed control: 5 levels (0.5×, 1×, 2×, 4×, 8×)
- Play / Pause / Reset
- Algorithm switcher (horizontal chip row per category)

---

## Explicitly Out of Scope for v1

- User accounts, authentication, profiles
- Backend, API calls, network layer
- Persistence beyond trivial in-session state (no Room, no SharedPreferences data)
- Social features: sharing, leaderboards, comments
- iOS or Kotlin Multiplatform
- Dynamic programming visualizations
- Monetization, ads, in-app purchases
- Selection Sort, Shell Sort (in prototype but cut from v1)
- Weighted grid editing for Dijkstra (edges are unweighted in v1)

---

## Development Philosophy

1. **Plugin-first.** `AlgorithmPlugin` is the only extension point. Resist the urge to add category-specific base classes or helper hooks unless the pattern genuinely demands it.

2. **One new file + one line.** Adding an algorithm means creating one new file (`BubbleSort.kt`) and adding one `@Provides @IntoSet` binding in `AlgorithmModule.kt`. No changes to Engine, ViewModel, or UI screens.

3. **No UI in the logic layer. Ever.** If you find yourself importing `androidx` in an algorithm file, the architecture is broken. Stop and fix it.

4. **No algorithm enum or `when`-chain in ViewModel or Engine.** If a ViewModel switches on algorithm name, the plugin contract is violated.

5. **Prototype fidelity.** The HTML prototypes (maintained in a separate repository) are the visual and interaction reference. Match their color states, animation timing, and metrics panel structure.

6. **Step granularity convention.** One `VisualizationStep` = one atomic decision a human would narrate aloud ("comparing index 2 and 3", "marking cell (3,4) visited"). Consistent granularity makes the speed slider coherent across all algorithms. Enforced in `steps()` implementation, not in the engine.

7. **`VisualizationStep` must be self-sufficient for stateless rendering.** Each `VisualizationStep` must carry enough data for a Composable to draw a complete, correct frame without replaying prior steps. For sorting: full array snapshot per step. For grid: full 9×9 cell state map. For tree: full node state map + topology. Validated with JVM tests before any canvas Composable is written.

8. **Metrics panel driven by plugin metadata, not screen-hardcoded labels.** `AlgorithmPlugin.metricLabels` declares the 3 metric slot names; the screen renders whatever the plugin declared. Never hardcode "Comparisons / Swaps / Passes" in a Composable.

9. **Complexity as structured data, not display strings.** `AlgorithmPlugin` carries structured complexity fields (`bestCase`, `averageCase`, `worstCase`, `spaceComplexity`) as enums, not ad-hoc strings. This makes future sorting and filtering by complexity possible without parsing.

---

## Naming Conventions

### Rules (always apply)
- Classes are nouns: `PlaybackEngine`, `AlgorithmPlugin`
- Functions are verbs: `play()`, `reset()`, `fetchUsers()`
- Booleans start with `is` / `has` / `can`: `isPlaying`, `hasPermission`
- Constants are UPPER_SNAKE_CASE: `ROUTE_HOME`, `MAX_RETRY_COUNT`
- No abbreviations in class, function, or variable names (see exceptions below)

### Exceptions — keep as-is
- `AlgorithmPlugin`, `BubbleSort`, `BFS`, `DFS`, `Dijkstra` — proper CS nouns
- `id` — universally understood
- External API surface: `NavController`, `NavHost`, `NavDestination` (from `androidx.navigation`)

### Patterns in use
| Pattern | Example |
|---|---|
| `FeatureViewModel` | `SortingViewModel`, `GraphViewModel` |
| `FeatureScreen` (Composable entry point) | `SortingScreen`, `HomeScreen` |
| `FeatureUiState` | `SortingUiState` — defer until needed |
| `FeatureEvent` | `SortingEvent` — defer until needed |

### Deferred (no data layer in v1)
`UseCase`, `Repository`, `Impl`, `Dto`, `Entity`, `Mapper` — introduce only when a persistence or network layer exists.

---

## Visual Language (from validated prototypes)

| Token | Value | `MaterialTheme.colorScheme` slot |
|---|---|---|
| Background | `#0D0D0D` | `background` |
| Surface | `#1A1A1A` | `surface` |
| Accent green (sorted/visited/positive) | `#7CB99A` | `primary` |
| Accent peach (comparing/visiting/active) | `#E8917A` | `secondary` |
| Accent purple (pivot/height metric) | `#B8A4E8` | `tertiary` |
| Accent amber (complexity O(n²) / reads) | `#E8C47A` | `error` |
| Default element | `#2E2E2E` | `surfaceVariant` |
| Text primary | `#FFFFFF` | `onSurface` |
| Text secondary | `#AAAAAA` | `onSurfaceVariant` |
| Metric label | `#888888` | `outlineVariant` |
| Surface border | `#2E2E2E` | `outline` |
| Font: metrics | Space Mono | — |
| Font: labels | DM Sans | — |

**Animation timing:** 0.1–0.2s ease transitions for element state changes. Speed slider controls step delay (400ms at 0.5× → 8ms at 8×).

### Theme rules

1. **Never import raw color tokens in UI files.** Composables must read colors exclusively through `MaterialTheme.colorScheme.*`. Raw token imports (`AccentGreen`, `Background`, etc.) are forbidden in any file under `ui/`.

2. **Dynamic color is disabled.** `KodiflyaTheme` sets `dynamicColor = false`. The app's visualization semantics depend on fixed, predictable colors — green always means "sorted/visited", amber always means "complexity/reads". Dynamic color would override these with wallpaper-derived colors, breaking the visual contract.

3. **Canvas composables capture colors before the `Canvas { }` block.** `DrawScope` is not `@Composable`, so theme colors must be read in the composable body and passed as local variables into the drawing lambda:
   ```kotlin
   @Composable
   fun SortingCanvas(...) {
       val primary = MaterialTheme.colorScheme.primary  // read here
       Canvas(modifier) {
           drawRoundRect(color = primary, ...)           // use here
       }
   }
   ```

---

## Test Requirements

| Layer | Requirement |
|---|---|
| Logic (algorithms) | 100% unit-tested, JUnit5 + MockK, zero Android dependencies |
| Engine (playback) | Unit-tested with `kotlinx-coroutines-test` |
| UI | Compose UI tests for play/pause, speed change, algorithm switch |
| Integration | No database mocking (no DB in v1) |

Every algorithm's `steps()` output must be fully deterministic and testable without a device or emulator.

---

## Project Structure (target)

```
app/
  src/
    main/
      kotlin/com/kodiflya/
        core/
          plugin/   AlgorithmPlugin.kt, VisualizationStep.kt, VisualizationState.kt
          engine/   PlaybackEngine.kt, PlaybackEngineFactory.kt
        algorithms/
          sorting/  BubbleSort.kt, InsertionSort.kt, MergeSort.kt, QuickSort.kt
          graph/    BFS.kt, DFS.kt, Dijkstra.kt
          trees/    BSTInorder.kt, BSTPreorder.kt, BSTPostorder.kt
        di/         AlgorithmModule.kt (Hilt multibinding registry)
        ui/
          screens/
            home/     HomeScreen.kt, HomeViewModel.kt, MiniVisualization.kt
            sorting/  SortingScreen.kt, SortingViewModel.kt, SortingCanvas.kt
            graph/    GraphScreen.kt, GraphViewModel.kt, GraphCanvas.kt
            trees/    TreeScreen.kt, TreeViewModel.kt, TreeCanvas.kt
          navigation/ AppNavigation.kt
          component/  AlgorithmChipRow.kt, ComplexityCardsRow.kt, ControlsRow.kt,
                      MetricCard.kt, ScreenHeader.kt,
                      KodiflyaIcons.kt, AlgorithmIconSource.kt, AlgorithmIconRegistry.kt
          theme/      Theme.kt, Color.kt, Type.kt
    test/             (mirrors main — unit tests)
    androidTest/      (Compose UI tests)
```
