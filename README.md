# jbang-catalog
Useful utilities for running in JBang

## Installation

```shell
jbang catalog add --name teggr https://github.com/teggr/jbang-catalog/blob/main/jbang-catalog.json
```

## Catalog

### serve alias
A simple HTTP file server for local development. Serves files from the current directory on the specified port (default: 8282).

**Example usage:**
```shell
jbang serve@teggr
```

### mcp-stdio template
A Java Model Context Protocol (MCP) server template using standard input/output. Useful for building MCP-compatible tools. It will also output a mcp.json file for adding to your copilot mcp file: https://code.visualstudio.com/docs/copilot/chat/mcp-servers

**Example usage:**
```shell
jbang init --template=mcp-stdio my-mcp-server
cd my-mcp-server
jbang my-mcp-server.java
```

