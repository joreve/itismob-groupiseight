# Adapted (Android, Java + XML)

Package: `com.itismob.s03.groupiseight.adapted`

## Implemented (Modules A-D)
| Module | Screens / classes |
|---|---|
| A. Registration & Login | `LoginActivity`, `RegisterActivity`, `helpers/AuthManager` (SharedPreferences), `helpers/InputValidator` |
| B. Home | `HomeActivity`, `layout_section_header.xml`, `layout_category_tile.xml` |
| C. Media Library / Browse | `ExploreActivity`, `adapters/MediaAdapter`, `item_media.xml` |
| D. Search & Filter | inside `ExploreActivity`, logic in `helpers/MediaSearchHelper` |

`MainActivity` has no UI. It only routes to Home (already logged in) or Login.

## Package layout
```
activities/  one Activity per screen
adapters/    RecyclerView adapters
models/      plain data classes (MediaItem, MediaCategory, ...)
data/        SampleMediaData: local sample data (swap for API/database later)
helpers/     auth, validation, search, window-inset helpers
```

## Placeholders (Modules E-J) - not connected yet
`MediaDetailsActivity`, `RatingActivity`, `ComparisonActivity`, `ReviewsActivity`,
`CommentsActivity`, `MyLibraryActivity`, `ProfileActivity`, plus `ReviewModel`, `ReviewAdapter`,
`ComparisonModel`, `CommentModel`, `CommentAdapter` and their XML layouts are empty shells.

When you start your module:
1. Add an `<activity>` entry for your screen in `AndroidManifest.xml` (placeholders are deliberately not registered).
2. Add your destination to `res/menu/menu_bottom_nav.xml` if it belongs in the bottom bar, and handle it in `HomeActivity` / `ExploreActivity`.
3. Media cards (`MediaAdapter`) have no click listener yet. Module E can add one that opens `MediaDetailsActivity`.

## Theming
Change colors in `res/values/colors.xml` and the mapping in `res/values/themes.xml`. Component and text
styles live in `res/values/styles.xml`. Layouts use `?attr/...` theme attributes, not raw colors.
