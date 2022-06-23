/*
 * Copyright 2021 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.java.migrate.apache.commons.codec

import org.junit.jupiter.api.Test
import org.openrewrite.Recipe
import org.openrewrite.java.JavaParser
import org.openrewrite.java.JavaParser.fromJavaVersion
import org.openrewrite.java.JavaRecipeTest

class ApacheBase64ToJavaBase64Test : JavaRecipeTest {
    override val recipe: Recipe
        get() = ApacheBase64ToJavaBase64()
    override val parser: JavaParser
        get() = fromJavaVersion()
            .classpath("commons-codec")
            .logCompilationWarningsAndErrors(true)
            .build()

    @Test
    fun toJavaBase64() = assertChanged(
        before = """
            import org.apache.commons.codec.binary.Base64;
            
            class Test {
                static byte[] decodeBytes(byte[] encodedBytes) {
                    return Base64.decodeBase64(encodedBytes);
                }
                static byte[] decodeToBytes(String encodedString) {
                    return Base64.decodeBase64(encodedString);
                }
                static String encodeToString(byte[] decodedByteArr) {
                    return Base64.encodeBase64String(decodedByteArr);
                }
                static byte[] encodeBase64(byte[] binaryData) {
                    return Base64.encodeBase64(binaryData);
                }
                static byte[] encodeBytesUrlSafe(byte [] encodeBytes) {
                    return Base64.encodeBase64URLSafe(encodeBytes);
                }
                static String encodeBytesUrlSafeString(byte [] encodeBytes) {
                    return Base64.encodeBase64URLSafeString(encodeBytes);
                }
            }
        """,
        after = """
            import java.util.Base64;
            
            class Test {
                static byte[] decodeBytes(byte[] encodedBytes) {
                    return Base64.getDecoder().decode(encodedBytes);
                }
                static byte[] decodeToBytes(String encodedString) {
                    return Base64.getDecoder().decode(encodedString);
                }
                static String encodeToString(byte[] decodedByteArr) {
                    return Base64.getEncoder().encodeToString(decodedByteArr);
                }
                static byte[] encodeBase64(byte[] binaryData) {
                    return Base64.getEncoder().encode(binaryData);
                }
                static byte[] encodeBytesUrlSafe(byte [] encodeBytes) {
                    return Base64.getUrlEncoder().withoutPadding().encode(encodeBytes);
                }
                static String encodeBytesUrlSafeString(byte [] encodeBytes) {
                    return Base64.getUrlEncoder().withoutPadding().encodeToString(encodeBytes);
                }
            }
        """
    )
}