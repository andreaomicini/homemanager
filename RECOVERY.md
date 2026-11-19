# How this repository was recovered

## The source

The original repository, `https://bitbucket.org/tuprologteam/homemanager.git`, is gone. Software
Heritage had archived it:

- visit of **9 January 2021, 08:51:43 UTC**, snapshot `659bdaed47c4372bf8b5df24fd34a2eef5198ab7`;
- default branch `master` at revision `9f6c138ea5d54f14b51ec64ac849f1667eaeb0ae`, directory
  `8ccf58dc3209595e8da1c7a2f194cc1c7dc40243`: 4825 files in 1441 directories;
- 97 commits, 11 branches: `master`, `PR-BEVILACQUA` and nine `TH-*`.

Browse it at
<https://archive.softwareheritage.org/browse/origin/directory/?origin_url=https://bitbucket.org/tuprologteam/homemanager.git&visit_type=git>.

## The recovery (2026-11-19)

Codex rebuilt a Git repository from the archive's objects, on Andrea Omicini's request, for
the two collaborators who had lost it. Its notes and reports are in [`recovery/`](recovery/):

- [`Recovery-notes.txt`](recovery/Recovery-notes.txt): the account of the recovery;
- [`archived-history.json`](recovery/archived-history.json): the 97 commits as Software Heritage
  records them (author, dates, parents, directory);
- [`archived-branches.json`](recovery/archived-branches.json): the 11 branch heads as archived;
- [`commit-map.json`](recovery/commit-map.json): each archived commit id → its id here;
- [`git-recovery-report.json`](recovery/git-recovery-report.json),
  [`recovery-report.json`](recovery/recovery-report.json),
  [`verification-report.json`](recovery/verification-report.json): the checks, and the trees
  from which the missing file was dropped;
- [`SHA256SUMS.txt`](recovery/SHA256SUMS.txt): the checksums of the bundle and of the source
  archive it delivered.

Software Heritage had not kept every file's content. Some third-party files were fetched again
from their official distributions (Google's Maven repository for the Android libraries, OpenCV's
3.0.0 release, the ENEA mirror of Software Heritage for directory data) and **accepted only when
their Git hash equalled the archived one**, so their bytes are the original bytes.

## What is missing

**One file**: `opencv-3.0.0.zip` (189 137 624 bytes, blob
`5dd4fef0224f1613c255e2b78c4ef3bf107eb669`), committed by Jacopo Ameli on 2015-12-10 and kept in
nine trees. Its content was not archived and could not be found again with that hash. It was
removed from every revision, and the history rewritten from there: commit ids change wherever
that file, or an ancestor carrying it, is involved. The unpacked OpenCV sources and the
`opencv-300.jar` the code uses are present.

Also missing, because a Git archive does not hold them: Bitbucket's issues, pull-request
discussions and wiki, if there were any.

## What was checked here

On 2026-11-19, before anything was added:

- the bundle's SHA-256 equals the one in `SHA256SUMS.txt`; `git bundle verify` and `git fsck`
  pass;
- **all 97 commits** have the archived author, author date, committer date and parents (through
  `commit-map.json`);
- **53 of the 97 keep their original commit id**, the ones the missing file does not reach:
  for those, Git's hashes prove that every byte, message and tree is the archived one. The other
  44 start at Ameli's commits of 2015-12-10;
- the 11 branch heads are those of `archived-branches.json`, through `commit-map.json`.

## After the archive

On top of `master` as archived (tag `v0-bitbucket`), the commits of 2026 are marked `claude:`:
the build (`v1-builds`), then the repository around the code. The tags `th-*` on the old
commits are annotations added in 2026; they move nothing.
