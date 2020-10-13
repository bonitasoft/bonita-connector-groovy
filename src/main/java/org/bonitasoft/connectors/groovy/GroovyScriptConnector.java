/*
 * Copyright (C) 2009 - 2020 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.groovy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.bonitasoft.engine.expression.ExpressionConstants;

import groovy.lang.Binding;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;

/**
 * @author Matthieu Chaffotte
 */
public class GroovyScriptConnector extends AbstractConnector {

    public static final String SCRIPT = "script";

    public static final String CONTEXT = "variables";

    public static final String RESULT = "result";

    @Override
    protected void executeBusinessLogic() throws ConnectorException {
        String script = (String) getInputParameter(SCRIPT);
        Map<String, Object> variables = getVariables();
        Binding binding = new Binding(variables);
        GroovyShell shell = new GroovyShell(Thread.currentThread().getContextClassLoader(), binding);
        try {
            Object result = shell.evaluate(script);
            setOutputParameter(RESULT, result);
        } catch (GroovyRuntimeException gre) {
            throw new ConnectorException(gre);
        }
    }

    @Override
    public void validateInputParameters() throws ConnectorValidationException {
        String script = (String) getInputParameter(SCRIPT);
        if (script == null) {
            throw new ConnectorValidationException(this, "The script is null");
        }
    }

    private Map<String, Object> getVariables() {
        List<List<Object>> context = (List<List<Object>>) getInputParameter(CONTEXT);
        Map<String, Object> variables = new HashMap<>();
        if (context != null) {
            context.stream()
                    .filter(rows -> rows.size() == 2)
                    .filter(rows -> rows.get(0) != null)
                    .forEach(rows -> {
                        Object keyContent = rows.get(0);
                        Object valueContent = rows.get(1);
                        String key = keyContent.toString();
                        if (ExpressionConstants.API_ACCESSOR.getEngineConstantName().equals(key)) {
                            variables.put(key, getAPIAccessor());
                        } else {
                            variables.put(key, valueContent);
                        }
                    });
        }
        return variables;
    }

}
