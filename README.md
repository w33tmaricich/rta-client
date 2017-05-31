# rta-client

A Clojure library designed to assist in RTA Host communication.

Wait a minute... RTA? If you are here I am assuming you have read
[Linux Appliance Design](https://www.amazon.com/Linux-Appliance-Design-Hands-Appliances/dp/1593271409)
and for some reason want an RTA implementation in clojure! I tell you what. Wow.
I cant believe that someone is looking for such a niche library. Please shoot
me a message at w33tmaricich@gmail.com if you use this. My mind will be blown
that such a person exists other than me.

## Usage

In your project.clj:

```
[com.skyline/rta-client "0.1.1"]
```

In your namespace:
```
(ns my-namespace.core
  (:require [rta-client.connections :as rta]))
```

For more detailed information about functions, see the documentation in `/doc`.

## License

Copyright © 2016 Alexander Maricich
