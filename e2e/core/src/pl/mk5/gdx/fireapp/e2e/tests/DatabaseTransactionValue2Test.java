/*
 * Copyright 2019 mk
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

package pl.mk5.gdx.fireapp.e2e.tests;

import com.badlogic.gdx.graphics.g2d.Batch;

import pl.mk5.gdx.fireapp.GdxFIRDatabase;
import pl.mk5.gdx.fireapp.e2e.runner.E2ETest;
import pl.mk5.gdx.fireapp.functional.Consumer;
import pl.mk5.gdx.fireapp.functional.Function;

public class DatabaseTransactionValue2Test extends E2ETest {

    @Override
    public void action() {
        final String value = "test-value";
        final String reference = "/test-trans";

        GdxFIRDatabase.inst()
                .inReference(reference).setValue(value)
                .then(GdxFIRDatabase.inst().inReference(reference)
                        .transaction(String.class, new Function<String, String>() {
                            @Override
                            public String apply(String str) {
                                return str + "abc";
                            }
                        }))
                .then(GdxFIRDatabase.inst().inReference(reference).readValue(String.class))
                .then(new Consumer<String>() {
                    @Override
                    public void accept(String i) {
                        if (i.equals(value + "abc")) {
                            success();
                        }
                    }
                });
    }

    @Override
    public void draw(Batch batch) {
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void dispose() {
    }
}
