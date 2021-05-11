package data_structures.map;

@FunctionalInterface
public interface KeyPopulator<K extends Comparable<? super K>, V> {

	K key(V value);

}
