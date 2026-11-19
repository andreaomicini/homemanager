# CLAUDE.md

Guidance for Claude Code (claude.ai/code) working in this repository.

## What this is

**Home Manager**, the multi-agent platform for the intelligent home on TuCSoN and tuProlog
(DISI, Università di Bologna): its Bitbucket repository of 2014–2018, the workbench of the
theses on it, **recovered** on 2026-11-19 from Software Heritage, at Andrea Omicini's request,
for the two collaborators who had lost it. Product page: <https://homemanager.apice.unibo.it>.

**Read `README.md` and `RECOVERY.md` first.** This file covers what they do not say.

## The history is the archive

- **Everything up to `v0-bitbucket` is the archived history**: 97 commits, 11 branches, authors
  and dates as Software Heritage recorded them (checked, see `RECOVERY.md`). Never rewrite,
  rebase, squash or drop any of it, and never delete a `TH-*` or `PR-*` branch. The one
  rewrite already made, the removal of the unarchived `opencv-3.0.0.zip`, is recorded in
  `recovery/commit-map.json`.
- **Everything was kept, built files included** (APKs, Gradle intermediates, `.class`, the
  OpenCV sources of 2016, the Eclipse `.metadata`): Andrea's decision, 2026-11-19. About 475 MB;
  the largest file is 66 MB, under GitHub's 100 MB limit.
- The `th-<student>-<year>` tags mark where each thesis entered `master`, from the thesis
  records on APICe (`Thesis.<Name>`); their messages name the record. A new fact about a
  thesis goes into a new tag message or the README, never into the old commits.
- Credentials of the time are in the code (a Twitter consumer key and access token in
  `social/TwitterAgent.java`, mail addresses; the passwords are empty). They were public on
  Bitbucket and are public on Software Heritage; they stay, as history.

## Building

- **`./build.sh` with a Java 8 JDK** (`JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home`,
  Homebrew's `zulu@8` cask). Not a later JDK: `SmartFridge` and `SmartPantry` use
  `sun.misc.BASE64Encoder`, and `--release 8` on JDK 11/17 does not expose it. The script
  refuses a javac that is not 1.8.
- Each part compiles from its own `src/` against the jars inside it, `-encoding UTF-8`; classes go
  to `_build/` (ignored). Expected: HomeManager 433 classes, SmartComponentInfrastructure 28,
  SmartFridge 51, SmartPantry 51, SmartMixer 26, SmartOven 13, SmartWardrobe 54.
- The 15 Latin-1 sources (6 HomeManager, 9 SmartComponentInfrastructure) were re-encoded to UTF-8
  in `v1-builds`; their classes were checked byte-identical. One string in
  `dbmanagement/TucsonDatabase.java` already held a broken character (`Non � possbile`) in the
  archive: it is history, leave it.
- **`MyAppHomeManager` (Android, Gradle 2.8) is not built** and has no CI job. Reviving it would
  be a project of its own: ask Andrea first.

## CI

`.gitlab-ci.yml`: one job per Java part, image `azul/zulu-openjdk:8`, each running
`./build.sh <part>` only when a push touches the part, `build.sh` or the CI. Run the same
locally before every push.

## Committing

- The `claude:` prefix on every commit I make; commits touching only notes, `desc/`, `logo/`
  or `icon/` carry `[skip ci]`.
- **GitLab DISI refuses any push over 60 MiB** (`pack exceeds maximum allowed size`): a push of a
  large file needs splitting, as the first push did (see `.ai/HANDOFF.md`).
- **GitHub is fed by GitLab**: a push mirror (SSH, GitLab's key as GitHub deploy key with write) copies every
  push GitLab receives, the collaborators' too. This repo is the first with one; the others push to both
  from the local `origin`. GitLab's API cannot set SSH host keys: a new mirror is made in the web UI
  (*Detect host keys*).
- **Remotes**: see `.ai/HANDOFF.md`. One `origin` pushing to GitLab DISI and GitHub; a read-only
  mirror in `~/GitHub/andreaomicini/homemanager`, pulled after every push.
- `desc/homemanager.txt`: line 1 the description, last line the APICe alias, the source of the
  GitLab and GitHub descriptions. `logo/logoHM2.jpg` is the product logo from the APICe record;
  `icon/homemanager.icns` and the GitLab avatar
  `logo/homemanager-avatar.png` (512 px: GitLab takes at most 200 KiB) were made from it, on a
  transparent square: a product has no kit *mark*, so its logo stands in.

## Content

The code of 2014–2018 is not improved. Bugs found are reported, not fixed; form is fair game
only outside the historical trees. Propose, and let Andrea decide.
