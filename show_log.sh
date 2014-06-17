#!/bin/bash

adb logcat -v time -s yeojoy XXXXX AndroidRuntime System.err System.out DustFragment DustActivity WebParserService ImageAdapter DustUtils StartFragment SettingFragment $@

exit 0
