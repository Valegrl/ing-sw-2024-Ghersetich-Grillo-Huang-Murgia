# Codex Naturalis - Software Engineering Project

<img src="src/main/resources/it/polimi/ingsw/images/background/CodexNaturalisBackground.jpg" width="200" align="right" />

Codex Naturalis Game is the final test of **"Software Engineering"** course of **"Engineering of Computing Systems"** held at Politecnico di Milano (2023/2024).

**Teacher**: San Pietro Pierluigi

<br>

## The Team
* [Pietro Ghersetich](https://github.com/PietroGhersetich) | pietro.ghersetich@mail.polimi.it
* [Valerio Grillo](https://github.com/Valegrl) | valerio.grillo@mail.polimi.it
* [Jerry Huang](https://github.com/polimiJHuang) | jerry.huang@mail.polimi.it
* [Filippo Murgia](https://github.com/filippomurgia) | filippo.murgia@mail.polimi.it

## Implemented functionalities

### Main functionalities
| Functionality                    | Status |
|:---------------------------------|:------:|
| Basic rules                      |   ✅    |
| Complete rules                   |   ✅    |
| Socket                           |   ✅    |
| RMI                              |   ✅    |
| TUI _(Terminal User Interface)_  |   ✅    |
| GUI _(Graphical User Interface)_ |   ✅    |

### Advanced functionalities
| Functionality                | Status |
|:-----------------------------|:------:|
| Multiple Games               |   ✅    |
| Resilience to disconnections |   ✅    |
| Chat                         |   ✅    |
| Persistence                  |   ⛔    |


⛔ Not implemented &nbsp;&nbsp;&nbsp;&nbsp; ✅ Implemented

## Test cases
| Package    | Class, %     | Method, %     | Line, %       | Branches, %   |
|:-----------|:-------------|:--------------|:--------------|:--------------|
| Model      | 100% (30/30) | 98% (147/150) | 97% (643/662) | 84% (311/370) |
| Controller | 100% (3/3)   | 95% (91/95)   | 86% (674/777) | 70% (432/614) |

## Execution Instructions

### Requirements

Regardless of the operating system, you must have installed the following programs:
- Java 21
- JavaFX 21

### Instructions
1. Move to the directory where you have the JAR files and execute the server:
    ```shell
    java -jar PSP51_Codex_Naturalis_Server.jar -server_ip
    ```
2. Then execute the client:
    ```shell
    java -jar PSP51_Codex_Naturalis_Client.jar -self_ip
    ```

   Note that the client also accepts arguments at startup. In fact, it can also be started as:
    1. For TUI version:

        ```shell
        java -jar PSP51_Codex_Naturalis_Client.jar -self_ip -cli 
        ```
    2. For GUI version:

        ```shell
        java -jar PSP51_Codex_Naturalis_Client.jar -self_ip
        ```
