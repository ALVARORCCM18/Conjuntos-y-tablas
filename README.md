# MapaDispersionAbierta Hash Table Implementation

## Introduction
The `MapaDispersionAbierta` is a hash table implementation in Java that provides an efficient way to store and retrieve key-value pairs. It utilizes an open addressing strategy for collision resolution, ensuring that all entries can be accessed in a constant average time.

## Features
- **Open Addressing**: This implementation uses linear probing to handle collisions.
- **Dynamic Resizing**: The hash table automatically resizes when the load factor exceeds a threshold.
- **Support for Generics**: Allows for any type of key and value pair.

## Implementation Details
### Core Classes
1. **MapaDispersionAbierta**: Main class for the hash table.
   - `private K[] keys;`
   - `private V[] values;`
   - `private int size;`
   - `private int capacity;`
   - `private double loadFactorThreshold;`

### Key Methods
- **put(K key, V value)**: Inserts a key-value pair into the table.
- **get(K key)**: Retrieves the value associated with the given key.
- **remove(K key)**: Removes the key-value pair from the table.
- **resize(int newSize)**: Resizes the underlying array when necessary.

## Example Usage
```java
MapaDispersionAbierta<String, Integer> map = new MapaDispersionAbierta<>();
map.put("One", 1);
map.put("Two", 2);
System.out.println(map.get("One")); // Outputs: 1
map.remove("Two");
```

## Conclusion
The `MapaDispersionAbierta` is a robust and efficient hash table implementation, suitable for various applications where fast access times are critical. The use of dynamic resizing and open addressing makes it a flexible choice for developers.

## References
- [Java Collections Framework](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)
- [Data Structures and Algorithms in Java](https://www.amazon.com/Data-Structures-Algorithms-Java/dp/0133731889)

## License
This project is licensed under the MIT License.