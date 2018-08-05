/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */
package discord4j.store.service;

import discord4j.store.Store;
import discord4j.store.broker.BiVariateStoreBroker;
import discord4j.store.broker.StoreBroker;
import discord4j.store.broker.simple.SimpleBiVariateStoreBroker;
import discord4j.store.broker.simple.SimpleStoreBroker;
import discord4j.store.noop.NoOpStoreService;
import discord4j.store.primitive.LongObjStore;
import discord4j.store.util.StoreContext;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.io.Serializable;

/**
 * This represents a java service which provides stores. This can be loaded via
 * {@link discord4j.store.service.StoreServiceLoader} or it may be loaded manually.
 *
 * @see java.util.ServiceLoader
 * @see <a href="https://github.com/google/auto/tree/master/service">Google AutoService</a>
 * @see StoreServiceLoader
 * @see NoOpStoreService
 */
public interface StoreService {

    /**
     * This is used to check if this service can provide generic stores.
     *
     * @return True if possible, else false.
     * @see Store
     */
    boolean hasGenericStores();

    /**
     * This is called to provide a new store instance for the provided configuration.
     *
     * @param keyClass The class of the keys.
     * @param valueClass The class of the values.
     * @param <K> The key type which provides a 1:1 mapping to the value type. This type is also expected to be
     * {@link Comparable} in order to allow for range operations.
     * @param <V> The value type, these follow
     * <a href="https://en.wikipedia.org/wiki/JavaBeans#JavaBean_conventions">JavaBean</a> conventions.
     * @return The instance of the store.
     */
    <K extends Comparable<K>, V extends Serializable> Store<K, V> provideGenericStore(Class<K> keyClass, Class<V>
            valueClass);

    /**
     * This is used to check if this service can provide long-object stores.
     *
     * @return True if possible, else false.
     * @see LongObjStore
     */
    boolean hasLongObjStores();

    /**
     * This is called to provide a new store instance with a long key and object values.
     *
     * @param valueClass The class of the values.
     * @param <V> The value type, these follow
     * <a href="https://en.wikipedia.org/wiki/JavaBeans#JavaBean_conventions">JavaBean</a> conventions.
     * @return The instance of the store.
     */
    <V extends Serializable> LongObjStore<V> provideLongObjStore(Class<V> valueClass);

    /**
     * This is called to build an implementation-dependent store broker. The broker is used to manage many instances of
     * a specific store type by binding these instances to a specific long key.
     *
     * @param keyClass The key type for the stores to use.
     * @param valueClass The value type for the stores to use.
     * @return The {@link discord4j.store.broker.StoreBroker} implementation to use for this service.
     *
     * @see discord4j.store.broker.simple.SimpleStoreBroker
     */
    <K extends Comparable<K>, V extends Serializable> StoreBroker<Store<K, V>> provideStoreBroker(Class<K> keyClass,
            Class<V> valueClass);

    /**
     * This is called to build an implementation-dependent store broker. The broker is used to manage many instances of
     * a specific store type by binding these instances to a specific long key.
     *
     * <b>This is only called if this service has a valid long-obj store implementation!</b>
     *
     * @param valueClass The value type for the stores to use.
     * @return The {@link discord4j.store.broker.StoreBroker} implementation to use for this service.
     *
     * @see discord4j.store.broker.simple.SimpleStoreBroker
     */
    <V extends Serializable> StoreBroker<LongObjStore<V>> provideStoreBroker(Class<V> valueClass);

    /**
     * This is called to build an implementation-dependent bivariate store broker. The difference between bivariate
     * store brokers and normal store brokers are that these stores should be accessible via any of two potential keys.
     *
     * @param keyClass1 The first key option for stores.
     * @param keyClass2 The second key option for stores.
     * @param valueClass The value for the stores.
     * @return The {@link discord4j.store.broker.BiVariateStoreBroker} implemetation for this service.
     *
     * @see discord4j.store.broker.simple.SimpleBiVariateStoreBroker
     */
    <K1 extends Comparable<K1>, K2 extends Comparable<K2>, V extends Serializable>
        BiVariateStoreBroker<Store<K1, V>, Store<K2, V>> provideBiVariateStoreBroker(Class<K1> keyClass1,
           Class<K2> keyClass2, Class<V> valueClass);

    /**
     * This is called to build an implementation-dependent bivariate store broker. The difference between bivariate
     * store brokers and normal store brokers are that these stores should be accessible via any of two potential keys.
     *
     * <b>This is only called if this service has a valid long-obj store implementation!</b>
     *
     * @param valueClass The value for the stores.
     * @return The {@link discord4j.store.broker.BiVariateStoreBroker} implemetation for this service.
     *
     * @see discord4j.store.broker.simple.SimpleBiVariateStoreBroker
     */
    <V extends Serializable> BiVariateStoreBroker<LongObjStore<V>, LongObjStore<V>> provideBiVariateStoreBroker(
            Class<V> valueClass);

    /**
     * This is a lifecycle method called to signal that a store should allocate any necessary resources.
     *
     * @param context Some context about the environment which this service is being utilized in.
     * @return A mono, whose completion signals that resources have been allocated successfully.
     */
    Mono<Void> init(StoreContext context);

    /**
     * This is a lifecycle method called to signal that a store should dispose of any resources due to
     * an abrupt close (hard reconnect or disconnect).
     *
     * @return A mono, whose completion signals that resources have been released successfully.
     */
    Mono<Void> dispose();
}
