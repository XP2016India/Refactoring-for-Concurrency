# Refactoring-for-Concurrency

After initially importing this file, please make sure to run `gradle test` to bring down any dependencies.


## DESCRIPTION:
A walk through various scenarios in a codebase that works perfectly when there are only a few users in the system, but where subtle, irreproducible or hard-to-reproduce bugs appear under load. S let’s explore what makes this code concurrency-unsafe and what options we have for changing the implementation of the code without changing its expected behavior.

These are the scenarios to explore:

* One-time component initialization
* Stateful singletons
* Undocumented third-party libraries
* Switching from blocking to non-blocking collections
* Protecting critical regions of code
* Distributed atomic operations

## LEARNING OBJECTIVES:
* Can I safely make changes that will not break existing features?
* How much testing will give me confidence that the code works under load?
* At what layer (unit, integration, acceptance, other???) should I test?
* Code examples will be primarily in Java, but we might explore libraries and features of other relevant languages. This will be a hands-on workshop. All code will be in github. 

## SOFTWARE NEEDED FOR THIS WORKSHOP:
Participants are encouraged to have the following installed as a pre-requisite:

* A git client (to get the source)
* Java 8
* A Java editor / IDE (Eclipse, IntelliJ, Netbeans, vi, etc.)
* Gradle
