#!/bin/bash

adb logcat -v time -s yeojoy XXXXX AndroidRuntime System.err System.out DustInfoDBHelper SqliteManager DustFragment DustActivity WebParserService ImageAdapter DustUtils $@

exit 0
