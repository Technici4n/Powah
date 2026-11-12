# Native cable traversal checks

This optional test add-on runs on a disposable Minecraft 26.1.2 / NeoForge 126 dedicated server with Java 25. It is excluded from the normal release. Never install it on a public or production server.

1. Put the Powah build under test and its dependencies in this directory's `mods/` folder.
2. Build with Gradle 9.2.1: `gradle -p tests/energy-regression jar`.
3. Add the resulting test JAR to a separate disposable server. Bind `server-ip=127.0.0.1`, use a fresh flat world, and start with `-Ditv.energy.fixture=true` and no players.
4. Run `energylagqa` in the server console, inspect `energy-qa.json`, then stop the test server.
5. Repeat against the unmodified build in another fresh server to compare allocations and timings.

The checks exercise actual cable forwarding to native transactional energy handlers: 24 starting offsets, conservation, abort restoration and subclass traversal overrides. Fictional barrels and cable instances exist only inside the disposable fixture. The final benchmark performs 1000 rejected transfers through an 8192-cable network, with two warmups and five measured runs. Benchmark times cover this workload only; they are not server TPS predictions or evidence that all lag is fixed.
