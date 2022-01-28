package dev.mslalith.focuslauncher.utils

import dev.mslalith.focuslauncher.data.models.AppDrawerViewType
import dev.mslalith.focuslauncher.data.models.ClockAlignment

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
    }

    object Defaults {
        const val QUOTES_LIMIT = 300
        const val DEFAULT_CLOCK_24_ANALOG_RADIUS = 30f

        object Settings {

            object General {
                const val DEFAULT_FIRST_RUN = true
                const val DEFAULT_STATUS_BAR = false
                const val DEFAULT_NOTIFICATION_SHADE = true
            }

            object AppDrawer {
                val DEFAULT_APP_DRAWER_VIEW_TYPE = AppDrawerViewType.GRID
                const val DEFAULT_APP_ICONS = true
                const val DEFAULT_SEARCH_BAR = true
                const val DEFAULT_APP_GROUP_HEADER = true
            }

            object Clock {
                const val DEFAULT_SHOW_CLOCK_24 = false
                val DEFAULT_CLOCK_ALIGNMENT = ClockAlignment.START
                const val DEFAULT_CLOCK_24_ANIMATION_DURATION = 1200
            }

            object LunarPhase {
                const val DEFAULT_SHOW_LUNAR_PHASE = false
                const val DEFAULT_SHOW_ILLUMINATION_PERCENT = false
                const val DEFAULT_SHOW_UPCOMING_PHASE_DETAILS = true
            }

            object Quotes {
                const val DEFAULT_SHOW_QUOTES = false
            }
        }
    }
}
