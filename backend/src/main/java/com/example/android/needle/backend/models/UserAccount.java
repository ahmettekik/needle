/*
 * Copyright 2015 Google Inc. All Rights Reserved.
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

package com.example.android.needle.backend.models;


import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * UserAccount entity.
 */
@Entity
public class UserAccount {

    /**
     * Unique identifier of this Entity in the database.
     */
    @Id
    private Long key;

    @Index
    private String email;



    /**
     * Returns a boolean indicating if the user is an admin or not.
     * @param user to check.
     * @return the user authorization level.
     */
    public static boolean isAdmin(final User user) {

        return true;
    }


    /**
     * Returns the user email.
     * @return the user email.
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Sets the user email.
     * @param pEmail the user email to set.
     */
    public final void setEmail(final String pEmail) {
        this.email = pEmail;
    }

}
