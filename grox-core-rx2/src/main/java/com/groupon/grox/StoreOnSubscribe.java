/*
 * Copyright (c) 2017, Groupon, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.groupon.grox;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Internal subscriber to a store's observable. It basically allows to unsubscribe from the store
 * when the observable is disposed.
 *
 * @param <STATE> the class of the the state of the store.
 */
final class StoreOnSubscribe<STATE> implements ObservableOnSubscribe<STATE> {
  private final Store<STATE> store;

  StoreOnSubscribe(Store<STATE> store) {
    this.store = store;
  }

  @Override
  public void subscribe(ObservableEmitter<STATE> emitter) throws Exception {

    //the internal listener to the store.
    Store.StateChangeListener<STATE> listener =
        emitter::onNext;
    emitter.setCancellable(() -> store.unsubscribe(listener));
    store.subscribe(listener);
  }
}
