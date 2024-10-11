# nc
*Yet another simple netcat clone in Java, for use in our networked systems lecture.*

It only supports TCP connections and optionally converts unix-style line endings to an HTTP-compatible CRLF.

## Usage

Download the source code [`nc.java`](nc.java), compile it with `javac`, and execute the command afterwards:
```
$ wget https://raw.githubusercontent.com/ech0-de/nc/refs/heads/main/nc.java
$ javac nc.java
$ java nc
Usage: java nc [-Cl] [destination] [port]
  -C   Use CRLF for EOL sequence
  -l   Bind and listen for incoming connections
```

Alternatively, you can download the precompiled [`nc.jar`](https://github.com/ech0-de/nc/releases/latest/download/nc.jar), and invoke it directly:
```
$ wget https://github.com/ech0-de/nc/releases/latest/download/nc.jar
$ java -jar nc.jar
Usage: java nc [-Cl] [destination] [port]
  -C   Use CRLF for EOL sequence
  -l   Bind and listen for incoming connections
```

Modern java versions (>=11) even allow you to directly invoke the [`nc.java`](nc.java) file, having it compiled on the fly:
```
$ wget https://raw.githubusercontent.com/ech0-de/nc/refs/heads/main/nc.java
$ java nc.java
Usage: java nc [-Cl] [destination] [port]
  -C   Use CRLF for EOL sequence
  -l   Bind and listen for incoming connections
```

## License
MIT
