package org.everit.json.schema.internal;

import org.json.JSONObject;
import org.json.JSONWriter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JSONPrinterTest {

    private StringWriter buffer;

    @Before
    public void before() {
        buffer = new StringWriter();
    }

    private JSONObject actualObj() {
        return new JSONObject(buffer.toString());
    }

    @Test
    public void constructor() {
        new JSONPrinter(new JSONWriter(new StringWriter()));
    }

    private JSONPrinter subject() {
        return new JSONPrinter(buffer);
    }

    @Test
    public void keyValueDelegates() {
        JSONPrinter subject = subject();
        subject.object();
        subject.key("mykey");
        subject.value("myvalue");
        subject.endObject();
        assertEquals("myvalue", actualObj().get("mykey"));
    }

    @Test
    public void ifPresentPrints() {
        JSONPrinter subject = subject();
        subject.object();
        subject.ifPresent("mykey", "myvalue");
        subject.endObject();
        assertEquals("myvalue", actualObj().get("mykey"));
    }

    @Test
    public void ifPresentOmits() {
        JSONPrinter subject = subject();
        subject.object();
        subject.ifPresent("mykey", null);
        subject.endObject();
        assertNull(actualObj().opt("mykey"));
    }

    @Test
    public void ifTruePints() {
        JSONPrinter subject = subject();
        subject.object();
        subject.ifTrue("uniqueItems", true);
        subject.endObject();
        assertEquals(true, actualObj().getBoolean("uniqueItems"));
    }

    @Test
    public void ifTrueHandlesNullAsFalse() {
        JSONPrinter subject = subject();
        subject.object();
        subject.ifTrue("uniqueItems", null);
        subject.endObject();
        assertNull(actualObj().opt("uniqueItems"));
    }

    @Test
    public void ifTrueOmits() {
        JSONPrinter subject = subject();
        subject.object();
        subject.ifTrue("uniqueItems", false);
        subject.endObject();
        assertNull(actualObj().opt("uniqueItems"));
    }

    @Test
    public void ifFalsePrints() {
        JSONPrinter subject = subject();
        subject.object();
        subject.ifFalse("mykey", false);
        subject.endObject();
        assertEquals(false, actualObj().getBoolean("mykey"));
    }

    @Test
    public void ifFalseOmits() {
        JSONPrinter subject = subject();
        subject.object();
        subject.ifFalse("mykey", true);
        subject.endObject();
        assertNull(actualObj().opt("mykey"));
    }

    @Test @Ignore
    public void ifFalseHandlesNullAsTrue() {
        JSONPrinter subject = subject();
        subject.object();
        subject.ifFalse("mykey", null);
        subject.endObject();
        assertNull(actualObj().opt("mykey"));
    }

    @Test
    public void arraySupport() {
        JSONPrinter subject = subject();
        subject.array();
        subject.value(true);
        subject.endArray();
        assertEquals("[true]", buffer.toString());
    }

}