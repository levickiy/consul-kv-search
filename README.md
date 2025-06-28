# Consul KV Search


This project is a small [Quarkus](https://quarkus.io/) application that indexes key/value entries from a Consul server and exposes a simple fuzzy search interface. Search queries are stored in an embedded SQLite database so that recent history can be displayed in the UI.

## Building and running

The project uses Maven. To run the unit tests execute:

```bash
mvn test
```

During development you can start Quarkus in dev mode which automatically reloads changes:

```bash
mvn quarkus:dev
```

The application starts on port `8080`. Visiting `/search` provides a basic HTML search page. The `/api/search` endpoint returns JSON results.

## Consul configuration

Connection parameters for Consul are defined in `src/main/resources/application.properties`:

```
consul.host=${CONSUL_HOST:localhost}
consul.port=${CONSUL_PORT:8500}
```

You can override them either by editing the properties file or by setting the environment variables `CONSUL_HOST` and `CONSUL_PORT` when launching the application.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
