/*
 * Copyright 2018 mk
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
package pl.mk5.gdx.fireapp.ios.auth;

import org.robovm.pods.firebase.googlesignin.GIDSignIn;

import pl.mk5.gdx.fireapp.auth.GdxFirebaseUser;
import pl.mk5.gdx.fireapp.distributions.GoogleAuthDistribution;
import pl.mk5.gdx.fireapp.functional.Consumer;
import pl.mk5.gdx.fireapp.promises.FuturePromise;
import pl.mk5.gdx.fireapp.promises.Promise;

/**
 * Google authorization ios API
 *
 * @see GoogleAuthDistribution
 */
public class GoogleAuth implements GoogleAuthDistribution {

    private final GoogleAuthProvider googleAuthProvider = new GoogleAuthProvider();

    @Override
    public Promise<GdxFirebaseUser> signIn() {
        return FuturePromise.when(new Consumer<FuturePromise<GdxFirebaseUser>>() {
            @Override
            public void accept(FuturePromise<GdxFirebaseUser> gdxFirebaseUserFuturePromise) {
                googleAuthProvider.initializeOnce();
                googleAuthProvider.addSignInPromise(gdxFirebaseUserFuturePromise);
                GIDSignIn.sharedInstance().signIn();
            }
        });
    }

    @Override
    public Promise<Void> signOut() {
        return FuturePromise.when(new Consumer<FuturePromise<Void>>() {
            @Override
            public void accept(FuturePromise<Void> voidFuturePromise) {
                googleAuthProvider.initializeOnce();
                try {
                    GIDSignIn.sharedInstance().signOut();
                    voidFuturePromise.doComplete(null);
                } catch (Exception e) {
                    voidFuturePromise.doFail(e);
                }
            }
        });
    }

    @Override
    public Promise<Void> revokeAccess() {
        return FuturePromise.when(new Consumer<FuturePromise<Void>>() {
            @Override
            public void accept(FuturePromise<Void> voidFuturePromise) {
                googleAuthProvider.initializeOnce();
                googleAuthProvider.addDisconnectPromise(voidFuturePromise);
                GIDSignIn.sharedInstance().disconnect();
            }
        });
    }
}
