---
name: handoff
description: Close out a teaching block of the Real System course by updating docs/progress.md. Use when the student types /handoff or says they are done for the block.
---

Close out the current block.

1. Update docs/progress.md:
   - set "Current position" to where we stopped: the day, block and step
     (e.g. "Day 12 morning — step 3, task 4 of 5"), or the next block if finished,
   - add one line to the log: date, day and block, what I built, decisions made,
   - list anything unfinished under "Open items", plus concepts I struggled with
     under "Revisit" so later blocks can reinforce them.
2. If a new known risk came up (the plan marks these), add it to docs/known-risks.md.
3. Suggest a commit message and show the git commands, but let me run them.
4. Keep the update short; this file is read at the start of every session.