# ProxCtrlJ

ProxCtrlJ is a personal project aimed at creating a cross-platform Java application that allows users to connect to Proxmox servers using a desktop client. The goal of this project is to replicate the functionalities of the Proxmox Web UI on a desktop application, providing a convenient and efficient way to manage Proxmox servers.

## Features

- **Cross-Platform**: Developed in Java, allowing the application to run on any platform that supports the Java Runtime Environment (JRE).
- **Proxmox API Integration**: Connect to Proxmox servers using the official API, enabling full administrative control directly from the desktop.
- **Comprehensive Functionality**: Perform all tasks available in the Proxmox Web UI, including managing clusters, nodes, virtual machines, containers, and more.
- **User-Friendly Interface**: Designed with a focus on usability, making it easy to manage your Proxmox infrastructure without needing to rely on a web browser.

## Installation

To run ProxCtrlJ, you will need to have the following prerequisites:

- Java Runtime Environment (JRE) 8 or higher.
- Internet access to connect to your Proxmox server.
- Properly configured Proxmox API tokens or user credentials.

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/ProxCtrlJ.git
    ```

2. Navigate to the project directory:
    ```bash
    cd ProxCtrlJ
    ```

3. Build the project using your preferred Java build tool (e.g., Maven or Gradle).

4. Run the application:
    ```bash
    java -jar target/ProxCtrlJ.jar
    ```

## Usage

After launching the application, you will be prompted to enter your Proxmox server credentials. Once connected, you can navigate through the different tabs to manage clusters, nodes, VMs, and other resources.

## Contributing

Contributions are welcome! If you have ideas, suggestions, or want to help improve the project, feel free to submit a pull request or open an issue.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## Acknowledgments

- Thanks to the Proxmox team for providing an excellent API that makes projects like this possible.
- Inspiration for this project came from the desire to have a more integrated desktop experience for managing Proxmox servers.
