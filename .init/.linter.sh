#!/bin/bash
cd /home/kavia/workspace/code-generation/note-keeper-6ffde0fb/frontend_notes_app
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

