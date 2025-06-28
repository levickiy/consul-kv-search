# Consul KV Search

This project is a Quarkus-based application that provides search capabilities over Consul's key-value store. It includes a small SQLite database for search history and fuzzy matching support via Java String Similarity.

## Building and Testing

The project uses **Maven** to manage dependencies, build the application and run tests. Make sure Maven is installed on your system before executing build commands.

To build the project and execute the test suite:

```bash
mvn clean install
```

If Maven is not available, you can install it using your system package manager or by running the provided script:

```bash
./install-maven.sh
```

After installation, re-run the Maven command above to build and test the project.

