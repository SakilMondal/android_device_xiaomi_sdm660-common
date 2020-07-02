ifeq ($(call is-board-platform-in-list,trinket), true)
TARGET_USES_NON_LEGACY_POWERHAL := false
else
TARGET_USES_NON_LEGACY_POWERHAL := true
endif
