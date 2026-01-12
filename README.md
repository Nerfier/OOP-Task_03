
Task 1:
To run the first task go into its directory and open the src folder. Then execute the following command in the terminal:
```
javac .java && java Main
```
----
Task 2 and 3:
At first I had some trouble with migrating the gradle projects to the \a01\ folder. When I tried to execute the project in the new location it would just execute the old project meaning that for example the files from task 3 would be stored in the old location as well. I believe that I now fixed this issue by deleting the cashes or more specifically the .gradle and build folders. Gradle then added them again after executing the program for the first time. It should now work normally as intended.
To run task 2 and 3 go into the directory of the respective task and execute the following command in the terminal:
```
./gradlew run --console plain -q
```
<details>
<summary>Run this commands if the execution of the code doesn't work.<\summary>
```
.\gradlew clean run --console plain -q

```
<\details>
