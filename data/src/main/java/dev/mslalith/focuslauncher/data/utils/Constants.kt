package dev.mslalith.focuslauncher.data.utils

import dev.mslalith.focuslauncher.data.model.AppDrawerViewType
import dev.mslalith.focuslauncher.data.model.ClockAlignment
import dev.mslalith.focuslauncher.data.model.places.City

object Constants {
    object DataStore {
        const val THEME_DATASTORE = "theme_datastore.pb"
        const val SETTINGS_DATASTORE = "settings_datastore.pb"
    }

    object Database {
        const val APP_DB_NAME = "focus_launcher_db"
        const val APPS_TABLE_NAME = "apps"
        const val FAVORITE_APPS_TABLE_NAME = "favorites_apps"
        const val HIDDEN_APPS_TABLE_NAME = "hidden_apps"
        const val QUOTES_TABLE_NAME = "quotes"
        const val CITIES_TABLE_NAME = "cities"
    }

    object Defaults {
        const val QUOTES_LIMIT = 300
        const val QUOTES_LIMIT_PER_PAGE = 150
        const val DEFAULT_CLOCK_24_ANALOG_RADIUS = 30f

        object Settings {

            object General {
                const val DEFAULT_FIRST_RUN = true
                const val DEFAULT_STATUS_BAR = false
                const val DEFAULT_NOTIFICATION_SHADE = true
                const val DEFAULT_IS_DEFAULT_LAUNCHER = false
            }

            object AppDrawer {
                val DEFAULT_APP_DRAWER_VIEW_TYPE = AppDrawerViewType.GRID
                const val DEFAULT_APP_ICONS = true
                const val DEFAULT_SEARCH_BAR = true
                const val DEFAULT_APP_GROUP_HEADER = true
            }

            object Clock {
                const val DEFAULT_SHOW_CLOCK_24 = true
                val DEFAULT_CLOCK_ALIGNMENT = ClockAlignment.START
                val DEFAULT_CLOCK_24_ANIMATION_DURATION_RANGE = 300f..2400f
                const val DEFAULT_CLOCK_24_ANIMATION_DURATION = 2100
                val DEFAULT_CLOCK_24_ANIMATION_STEP = ((DEFAULT_CLOCK_24_ANIMATION_DURATION_RANGE.endInclusive - DEFAULT_CLOCK_24_ANIMATION_DURATION_RANGE.start) / DEFAULT_CLOCK_24_ANIMATION_DURATION_RANGE.start).toInt() - 1
            }

            object LunarPhase {
                const val DEFAULT_SHOW_LUNAR_PHASE = true
                const val DEFAULT_SHOW_ILLUMINATION_PERCENT = true
                const val DEFAULT_SHOW_UPCOMING_PHASE_DETAILS = true
                val DEFAULT_CURRENT_PLACE = City(
                    id = -1,
                    name = "-",
                    latitude = 0.0,
                    longitude = 0.0
                )
            }

            object Quotes {
                const val DEFAULT_SHOW_QUOTES = false
            }
        }
    }
}
