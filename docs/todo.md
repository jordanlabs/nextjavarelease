# Next Java Release

A Twitter bot that tweets when there is a percentage increase
of the amount of time between the last and next Java release.

⬛⬛⬛⬛⬛⬛⬜⬜⬜⬜⬜⬜⬜⬜⬜ 40%

Average is 18th of the month

## Requirements

- Check once per day if the percentage has changed. Tweet the
  new percentage.
- Work out what the current Java release is and its release date.
- Lookup whether JDK is LTS version (subject to change)
  https://www.oracle.com/java/technologies/java-se-support-roadmap.html

## Notes

- Release is on a six-month schedule.
  - Able to guess when the next release would be from the average
    of the previous dates.
  - When a JDK version reaches GA and today's date has passed that
    date then look at the next Java version.
    
## Users to Follow

- @java
- @jhy (jsoup author)