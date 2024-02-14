# ♕ BYU CS 240 Chess

[Chess Server UML](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBF+wAIIgQKLl0wATeQCNgXFDA3bMmdlAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegt9KAAdNABvfMp7AFsUABoYXDVvaA06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vq9LiaoFpg2joQASkw2YfcxvtEByLkwRWVVLnj2FDAAVQKFguWDq5uVNQvDbTxMgAUQAMsC4OkYItljAAGbmSrQgqYb5KX5cAaDI5uUaecYiFTxNAWBAIQ4zE74s4qf5o25qeIgab8FCveYw4DVOoNdbNL7ydF3f5GeIASQAciCWFDOdzVo1mq12p0YJL0ilkbQcSMPIIaQZBvSMUyWYEFBYwL53hUuSgBdchX9BqK1VLgTKtUs7XVgJbfOkIABrdBujUwP1W1GChmY0LYyl4-UTIkR-2BkNoCnHJMEqjneORPqUeKRgPB9C9aKULGRYLoMDxABMAAYW8USmWM+geugNCYzJZrDZoNJHjBQRBOGgfP5Aph62Ei9W4olUhlsjl9Gp8R25SteRsNL20P3TOYrLYzChQ5P2JZmDY-AEgsgG-9i6uEjIwcD0sDN23Lhd3sdMKyzPsBwvYdpg0Cc3BgABxO1MSfOdXxCZhBk-eIEkQv9N3YO1ii7cDMEg88h1sbALCgbAEAMOAzQMZDqlnF8FzfJcomoEs1zSLJciI6oSLAzMO2ElAJTtE8z0HS8bEcFAyQgbwYAAKQgJAZ1YgwbG0BBQCDTjMI-FdcKSZ5BJySTRKtbs0A7RcwDgCAEGgOpJLFGRZKgqibDMYBnEQFA8AjbB6MIPUZzQjjnLM3ivx-cF-03IxyNPTAgA](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1BpNFo3LH3KRPNQKKIASQAcu8rL9-mTppT5otli0YJykvEwbQ4RjQpDGRIoniUAThHowJ4SZN+fS9grmS8xVy3jypdryTBgOrPEkIABrdBGiWW60QhkHR6BWGbfoujW2h1oNHwmGhOGRP02+3oYOUUPwZDoMBRABMAAY03l8lb-dG0O10DJNDp9IYjNBeMcYB8IHY3EYvD4-ImgmHWBG4olUhkVFIXGgs3yLYK6u1w902+jfTnPEnCAStVMKbMhesZeJmfKPUcTkSoHBBjI58AEBJFzq3XqPQbWeRPt9eVd+cuanUYAAxKzxACyZpgAHUAAkTTeM0AQAXhgCIhw4LdoS9Sd4SiQon2HFdR2DWUYDgnEqBPJA5F8WJ0IWUYkDQbB1WI18FkqBxoBQGRqKpBZdTua8EN6dgojw5BCP7Mg9AVTCN0xK8Hh3MA1VzQNzzKNjsU9Z5b3eL4fkjANHU-H9IwA4CrFAmCYEg6DUNg914O9KcERgbNrU0-MRNUTjxyiGcHNjboen8JNUwzLN3LzAs0CLbRdAMYwdBQR0ay0fRmAbbxfEwHzW16Ds+HvJI3jSdJewkfs8kCwNPOZdcbJGEpyIJCg+1cUZivQNcfWcuULJxcjeII1V7LzBresDBT9R6Q1Mq+bKPy-X8Zz0kCNLzYyoMaoMcM9KykJgCQhI9Jz41czaHn7JJKCq0xfCLcd41S5MYHTTMCjKKRgGOKIACJYuOGQYHizbtoeQFkQQcxXuC0LQEgWBrs4BgeFIcdoe4JguPEBHGF4ZhVtkeQlBUFli3CstD2rYYYAAcX5R5EqbFKW2YdaO1Jt5u3SLR+SK61oCQAAvZxXDcgaY0u9buJgewPsYtk0H6mSmt2sT2IkmAusIlBpKjWTlqGjjlNee91KM7TputWaDPmwNFoiZaYEvBWmRclqogmcWZEluX6agKJHD0DghcQkXkAcYQUXJx7Rma6zN3axUyZOEPJA+YYw5txSb11tTflZso2T4SpG18PclxgBQEFAO0C+fGBM5QDl+UmnSq+kVa9odyuKYThw3cQiMG-brAhe82m-Pu-Ie+GUHNDC0tjEoqBsAQVQDxVXwydrqnkuusr2w9mIEmSXKq-ZjVOZ5gg+bN9AsyrmuyjHLfN+sqIxdrT7Jel9XZfKyPxNw-CVbVhy34OS1vBHWd505gVrobXSQE5pWxMstZOw1ha+ids-CWQZP72y6J7KA3tSrIJssqAkcdRhXwrjBcOIZ5aKUfmgbIYASFkItBQxB2sWTsmNKacUkoG4wFIfya+0w86EjMqsGAAA1YQHxThvDIHwiITDpimVJPJVhlk-a+irovAkF0WrN2wa3LOfB8EDwCDdO6WYYCvSrtnV6UQACMKYADMAAWSo1iBH8jsVYjkKB3ArzKK9dxwjy6BLehMexr0YDj0nhFIw5gUAoggP4gAUhAciATVBGGLqXGmZj740G3nEU4zMD6mA5lAbmvM0D8xlgOAoNi+C3y6AUkWT8tAvylprOWbVv7R2Vj1OpgC8zALtqA1SD4IFlDrkbDUJtQJwKWgLFaUd9EbVQR09Bnd0rby9j7O+BCogACt0loEYfybOlQWGYN6bbaO5F6GxBLgkqAktIDnKMVc0RaixnsJgKcWIfBhATV4e8X4c9gAvNCaoEyRkYGm0aRbRpPy1oaJsic8iglhKYPdsc05WKdr91CNdIeljXqPQkM9FA3jXppPIoxQxqgtoKgBiiYG7jGneMRTEkscSdCQoTCqWAwhsBz3nKfeswi8lJgKRlLKOUMjqHwWipU888BBwQLcJONzsJRyiEeeevgNV7jPKMpSfyxpM1AlAiYKK1kiwmBIPgiSTiMR6bqvp+qXVGuDhTJOTcRq3ktRNKBDc7VYI2g3Z1hq3U6tWl6mNGr-55lNeG0BwbrVTUjOGw5MBICBidd62NeiI0RkIsARAQri0tM4iS26-kChWIpVSmlIqxUgGqWoeQuI1VE2ZR6VlQMonjyAA)https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1BpNFo3LH3KRPNQKKIASQAcu8rL9-mTppT5otli0YJykvEwbQ4RjQpDGRIoniUAThHowJ4SZN+fS9grmS8xVy3jypdryTBgOrPEkIABrdBGiWW60QhkHR6BWGbfoujW2h1oNHwmGhOGRP02+3oYOUUPwZDoMBRABMAAY03l8lb-dG0O10DJNDp9IYjNBeMcYB8IHY3EYvD4-ImgmHWBG4olUhkVFIXGgs3yLYK6u1w902+jfTnPEnCAStVMKbMhesZeJmfKPUcTkSoHBBjI58AEBJFzq3XqPQbWeRPt9eVd+cuanUYAAxKzxACyZpgAHUAAkTTeM0AQAXhgCIhw4LdoS9Sd4SiQon2HFdR2DWUYDgnEqBPJA5F8WJ0IWUYkDQbB1WI18FkqBxoBQGRqKpBZdTua8EN6dgojw5BCP7Mg9AVTCN0xK8Hh3MA1VzQNzzKNjsU9Z5b3eL4fkjANHU-H9IwA4CrFAmCYEg6DUNg914O9KcERgbNrU0-MRNUTjxyiGcHNjboen8JNUwzLN3LzAs0CLbRdAMYwdBQR0ay0fRmAbbxfEwHzW16Ds+HvJI3jSdJewkfs8kCwNPOZdcbJGEpyIJCg+1cUZivQNcfWcuULJxcjeII1V7LzBresDBT9R6Q1Mq+bKPy-X8Zz0kCNLzYyoMaoMcM9KykJgCQhI9Jz41czaHn7JJKCq0xfCLcd41S5MYHTTMCjKKRgGOKIACJYuOGQYHizbtoeQFkQQcxXuC0LQEgWBrs4BgeFIcdoe4JguPEBHGF4ZhVtkeQlBUFli3CstD2rYYYAAcX5R5EqbFKW2YdaO1Jt5u3SLR+SK61oCQAAvZxXDcgaY0u9buJgewPsYtk0H6mSmt2sT2IkmAusIlBpKjWTlqGjjlNee91KM7TputWaDPmwNFoiZaYEvBWmRclqogmcWZEluX6agKJHD0DghcQkXkAcYQUXJx7Rma6zN3axUyZOEPJA+YYw5txSb11tTflZso2T4SpG18PclxgBQEFAO0C+fGBM5QDl+UmnSq+kVa9odyuKYThw3cQiMG-brAhe82m-Pu-Ie+GUHNDC0tjEoqBsAQVQDxVXwydrqnkuusr2w9mIEmSXKq-ZjVOZ5gg+bN9AsyrmuyjHLfN+sqIxdrT7Jel9XZfKyPxNw-CVbVhy34OS1vBHWd505gVrobXSQE5pWxMstZOw1ha+ids-CWQZP72y6J7KA3tSrIJssqAkcdRhXwrjBcOIZ5aKUfmgbIYASFkItBQxB2sWTsmNKacUkoG4wFIfya+0w86EjMqsGAAA1YQHxThvDIHwiITDpimVJPJVhlk-a+irovAkF0WrN2wa3LOfB8EDwCDdO6WYYCvSrtnV6UQACMKYADMAAWSo1iBH8jsVYjkKB3ArzKK9dxwjy6BLehMexr0YDj0nhFIw5gUAoggP4gAUhAciATVBGGLqXGmZj740G3nEU4zMD6mA5lAbmvM0D8xlgOAoNi+C3y6AUkWT8tAvylprOWbVv7R2Vj1OpgC8zALtqA1SD4IFlDrkbDUJtQJwKWgLFaUd9EbVQR09Bnd0rby9j7O+BCogACt0loEYfybOlQWGYN6bbaO5F6GxBLgkqAktIDnKMVc0RaixnsJgKcWIfBhATV4e8X4c9gAvNCaoEyRkYGm0aRbRpPy1oaJsic8iglhKYPdsc05WKdr91CNdIeljXqPQkM9FA3jXppPIoxQxqgtoKgBiiYG7jGneMRTEkscSdCQoTCqWAwhsBz3nKfeswi8lJgKRlLKOUMjqHwWipU888BBwQLcJONzsJRyiEeeevgNV7jPKMpSfyxpM1AlAiYKK1kiwmBIPgiSTiMR6bqvp+qXVGuDhTJOTcRq3ktRNKBDc7VYI2g3Z1hq3U6tWl6mNGr-55lNeG0BwbrVTUjOGw5MBICBidd62NeiI0RkIsARAQri0tM4iS26-kChWIpVSmlIqxUgGqWoeQuI1VE2ZR6VlQMonjyAA)

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
