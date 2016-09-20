# Introduction to rta-client
 
Clojure wrapper around com.skyline.CameraControlServer.Utility.Communications/RTAClient.

This was a bit of code that I had written for a previous project that involved
needing to communicate directly with data that had been stored on an SFS. After
writing these functions within an application, I decided to turn it into a
library for future use.

I am not going to bother writing too much documentation because I'm pretty much
the only person here writing clojure here at skyline. Therefore, I'll just write
a couple of lines about each file and what it contains.

## connections.clj

This is the main intersection point between Hayden's library and my own. It
contains the following functions:

- `connect!`
  - Creates a connection to the RTA Server. This shouldn't ever be used by the
    library user. It is only for use with `quick-query!`.
  - `[host port]`
- `quick-query!`
  - Queries the database based upon the given string.
  - `[string]`
- `table-headers!`
  - Returns a set containing keywords of table headers for a given table.
  - `[table]`

## generate.clj

This file contains functions for generating different types of query strings
based upon clojure data structures. For example:

```
  (insert-query "Users" {:Name "Alex" :Age 23})
  yields
  "INSERT INTO Users (Name,Age) VALUES ('Alex','23');"
```

## globals.clj

Contains global variables to be used across the library.

- `HOST` :- psql -h flag. Hostname.
- `PORT` :- psql -p flag. Port number.

## utils.clj

Misc utility functions.
