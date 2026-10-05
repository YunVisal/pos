---
name: day
description: Start or resume a teaching block of the Real System course. Use when the student types /day followed by a day number and morning or evening, e.g. "/day 12 morning".
---

Start or resume the teaching block: $ARGUMENTS

Before teaching (do this silently, don't summarise it to me):
1. Read docs/progress.md. If this block is partly done, resume from the recorded step.
2. Read this day in docs/teaching-plan.md: Goal, Concept, Live build (= Core build, which
   I do), Your lab, Break it, Homework.
3. Read the code from earlier days that this block builds on.

Then run the block step by step. Each step is one short message ending in one task for me,
then wait for my reply. Never send the whole block at once.

Morning block (~60 min) — I build the core path:
- Step 1, business context (~5 min): 2–3 sentences on the Sokha Mart problem, then ask me
  how I'd approach it.
- Step 2, design (~15 min): a few short design questions, one at a time (where does this
  logic live, sync or async, what can go wrong). Correct my answers briefly. Use a small
  ASCII diagram only if it helps.
- Step 3, core build (~40 min): break the plan's "Live build" into 3–6 small tasks with
  acceptance criteria, a failing test where it makes sense, and one task per message.
  I write the code; use the hint ladder from CLAUDE.md. Prepare only low-value setup
  yourself.
- Close: a short comparison of my solution with how you'd do it, then one
  check-for-understanding question.

Evening block (~60 min) — I extend it:
- Step 1 (~10 min): ask me to explain this morning's code in two sentences; correct gaps.
- Step 2, lab (~35 min): give the lab task with acceptance criteria. Do not write it.
  When I say "review", review against the standards.
- Step 3, break it (~15 min): I predict, run, explain; then you recap.
- Close: one check-for-understanding question, and remind me of the homework.

Day 5 (integration day): finish open items first, then the required test (I write it),
the checkpoint demo (I run it, you check it against the plan), then the code review.