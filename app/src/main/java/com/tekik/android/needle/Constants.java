package com.tekik.android.needle;
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


import android.text.Html;

/**
 * API Keys, Client Ids and Audience Ids for accessing APIs and configuring
 * Cloud Endpoints. When you deploy your solution, you need to use your own API
 * Keys and IDs. Please update config.gradle to define them.
 */
public final class Constants {

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    static final String SENDER_ID = BuildConfig.SENDER_ID;

    /**
     * Web client ID from Google Cloud console.
     */
    static final String WEB_CLIENT_ID = BuildConfig.WEB_CLIENT_ID;

    /**
     * The web client ID from Google Cloud Console.
     */
    static final String AUDIENCE_ANDROID_CLIENT_ID =
            "server:client_id:" + WEB_CLIENT_ID;

    /**
     * The URL to the API. Default when running locally on your computer:
     * "http://10.0.2.2:8080/_ah/api/"
     */
    public static final String ROOT_URL = BuildConfig.ROOT_URL;



    /**
     * Default constructor, never called.
     */

    private Constants() { }

    public static final String eula = "Needle 1.0\n" +
            "Copyright (c) 2015 Ahmet Tekik\n" +
            "\n" +
            "END USER LICENSE AGREEMENT\n" +
            "\n" +
            "IMPORTANT: PLEASE READ THIS LICENSE CAREFULLY BEFORE USING THIS SOFTWARE.\n" +
            "\n" +
            "1. LICENSE\n" +
            "\n" +
            "By receiving, opening the file package, and/or using Needle 1.0(\"Software\") containing this software, you agree that this End User User License Agreement(EULA) is a legally binding and valid contract and agree to be bound by it. You agree to abide by the intellectual property laws and all of the terms and conditions of this Agreement.\n" +
            "\n" +
            "Unless you have a different license agreement signed by Ahmet Tekik your use of Needle 1.0 indicates your acceptance of this license agreement and warranty.\n" +
            "\n" +
            "Subject to the terms of this Agreement, Ahmet Tekik grants to you a limited, non-exclusive, non-transferable license, without right to sub-license, to use Needle 1.0 in accordance with this Agreement and any other written agreement with Ahmet Tekik. Ahmet Tekik does not transfer the title of Needle 1.0 to you; the license granted to you is not a sale. This agreement is a binding legal agreement between Ahmet Tekik and the purchasers or users of Needle 1.0.\n" +
            "\n" +
            "If you do not agree to be bound by this agreement, remove Needle 1.0 from your computer now and, if applicable, promptly return to Ahmet Tekik by mail any copies of Needle 1.0 and related documentation and packaging in your possession.\n" +
            "\n" +
            "2. DISTRIBUTION\n" +
            "\n" +
            "Needle 1.0 and the license herein granted shall not be copied, shared, distributed, re-sold, offered for re-sale, transferred or sub-licensed in whole or in part except that you may make one copy for archive purposes only. For information about redistribution of Needle 1.0 contact Ahmet Tekik.\n" +
            "\n" +
            "3. USER AGREEMENT\n" +
            "\n" +
            "3.1 Use\n" +
            "\n" +
            "Your license to use Needle 1.0 is limited to the number of licenses purchased by you. You shall not allow others to use, copy or evaluate copies of Needle 1.0.\n" +
            "\n" +
            "3.2 Use Restrictions\n" +
            "\n" +
            "You shall use Needle 1.0 in compliance with all applicable laws and not for any unlawful purpose. Without limiting the foregoing, use, display or distribution of Needle 1.0 together with material that is pornographic, racist, vulgar, obscene, defamatory, libelous, abusive, promoting hatred, discriminating or displaying prejudice based on religion, ethnic heritage, race, sexual orientation or age is strictly prohibited.\n" +
            "\n" +
            "Each licensed copy of Needle 1.0 may be used on one single computer location by one user. Use of Needle 1.0 means that you have loaded, installed, or run Needle 1.0 on a computer or similar device. If you install Needle 1.0 onto a multi-user platform, server or network, each and every individual user of Needle 1.0 must be licensed separately.\n" +
            "\n" +
            "You may make one copy of Needle 1.0 for backup purposes, providing you only have one copy installed on one computer being used by one person. Other users may not use your copy of Needle 1.0 . The assignment, sublicense, networking, sale, or distribution of copies of Needle 1.0 are strictly forbidden without the prior written consent of Ahmet Tekik. It is a violation of this agreement to assign, sell, share, loan, rent, lease, borrow, network or transfer the use of Needle 1.0. If any person other than yourself uses Needle 1.0 registered in your name, regardless of whether it is at the same time or different times, then this agreement is being violated and you are responsible for that violation!\n" +
            "\n" +
            "3.3 Copyright Restriction\n" +
            "\n" +
            "Needle logo by " + " " + Html.fromHtml("<a href=\"http://www.freepik.com/\">Freepik</a>") + " from " + Html.fromHtml("<a href=\"http://www.flaticon.com/\">Flaticon</a>") + " is licensed under " + Html.fromHtml("<a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\">CC BY 3.0</a>.") + " Made with " + Html.fromHtml("<a href=\"http://logomakr.com\" title=\"Logo Maker\">Logo Maker</a>") +
            "\n\n" +
            "This Software contains copyrighted material, trade secrets and other proprietary material. You shall not, and shall not attempt to, modify, reverse engineer, disassemble or decompile Needle 1.0. Nor can you create any derivative works or other works that are based upon or derived from Needle 1.0 in whole or in part.\n" +
            "\n" +
            "Ahmet Tekik's name and graphics file that represents Needle 1.0 shall not be used in any way to promote products developed with Needle 1.0 . Ahmet Tekik retains sole and exclusive ownership of all right, title and interest in and to Needle 1.0 and all Intellectual Property rights relating thereto.\n" +
            "\n" +
            "Copyright law and international copyright treaty provisions protect all parts of Needle 1.0, products and services. No program, code, part, image, audio sample, or text may be copied or used in any way by the user except as intended within the bounds of the single user program. All rights not expressly granted hereunder are reserved for Ahmet Tekik.\n" +
            "\n" +
            "3.4 Limitation of Responsibility\n" +
            "\n" +
            "You will indemnify, hold harmless, and defend Ahmet Tekik , its employees, agents and distributors against any and all claims, proceedings, demand and costs resulting from or in any way connected with your use of Ahmet Tekik's Software.\n" +
            "\n" +
            "In no event (including, without limitation, in the event of negligence) will Ahmet Tekik , its employees, agents or distributors be liable for any consequential, incidental, indirect, special or punitive damages whatsoever (including, without limitation, damages for loss of profits, loss of use, business interruption, loss of information or data, or pecuniary loss), in connection with or arising out of or related to this Agreement, Needle 1.0 or the use or inability to use Needle 1.0 or the furnishing, performance or use of any other matters hereunder whether based upon contract, tort or any other theory including negligence.\n" +
            "\n" +
            "Ahmet Tekik's entire liability, without exception, is limited to the customers' reimbursement of the purchase price of the Software (maximum being the lesser of the amount paid by you and the suggested retail price as listed by Ahmet Tekik ) in exchange for the return of the product, all copies, registration papers and manuals, and all materials that constitute a transfer of license from the customer back to Ahmet Tekik.\n" +
            "\n" +
            "3.5 Warranties\n" +
            "\n" +
            "Except as expressly stated in writing, Ahmet Tekik makes no representation or warranties in respect of this Software and expressly excludes all other warranties, expressed or implied, oral or written, including, without limitation, any implied warranties of merchantable quality or fitness for a particular purpose.\n" +
            "\n" +
            "3.6 Governing Law\n" +
            "\n" +
            "This Agreement shall be governed by the law of the United States applicable therein. You hereby irrevocably attorn and submit to the non-exclusive jurisdiction of the courts of United States therefrom. If any provision shall be considered unlawful, void or otherwise unenforceable, then that provision shall be deemed severable from this License and not affect the validity and enforceability of any other provisions.\n" +
            "\n" +
            "3.7 Termination\n" +
            "\n" +
            "Any failure to comply with the terms and conditions of this Agreement will result in automatic and immediate termination of this license. Upon termination of this license granted herein for any reason, you agree to immediately cease use of Needle 1.0 and destroy all copies of Needle 1.0 supplied under this Agreement. The financial obligations incurred by you shall survive the expiration or termination of this license.\n" +
            "\n" +
            "4. DISCLAIMER OF WARRANTY\n" +
            "\n" +
            "THIS SOFTWARE AND THE ACCOMPANYING FILES ARE SOLD \"AS IS\" AND WITHOUT WARRANTIES AS TO PERFORMANCE OR MERCHANTABILITY OR ANY OTHER WARRANTIES WHETHER EXPRESSED OR IMPLIED. THIS DISCLAIMER CONCERNS ALL FILES GENERATED AND EDITED BY Needle 1.0 AS WELL.\n" +
            "\n" +
            "5. CONSENT OF USE OF DATA\n" +
            "\n" +
            "You agree that Ahmet Tekik may collect and use information gathered in any manner as part of the product support services provided to you, if any, related to Needle 1.0.Ahmet Tekik may also use this information to provide notices to you which may be of use or interest to you.";
}
