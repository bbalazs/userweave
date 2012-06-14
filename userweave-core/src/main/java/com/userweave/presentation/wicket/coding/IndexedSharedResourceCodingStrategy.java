/*******************************************************************************
 * This file is part of UserWeave.
 *
 *     UserWeave is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     UserWeave is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with UserWeave.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2012 User Prompt GmbH | Psychologic IT Expertise
 *******************************************************************************/
package com.userweave.presentation.wicket.coding;


/**
 * coming from http://issues.apache.org/jira/browse/wicket-1666
 * <p>
 * 
 * 
 * indexed url encoding for shared resources with optional query parameters
 * <p/>
 * for example, with the following url
 * <pre>
 *   /mountpath/foo/bar/baz?name=joe&languages=java&languages=scala
 * </pre>
 * the parameters value map for the example will be
 * <p/>
 * <table border="1" cellpadding="4px">
 * <thead><tr><th>Key</th><th>Value</th></tr></thead>
 * <tbody>
 * <tr><td>"0"</th><td>"foo"</td></tr>
 * <tr><td>"1"</th><td>"bar"</td></tr>
 * <tr><td>"2"</th><td>"baz"</td></tr>
 * <tr><td>"name"</th><td>"joe"</td></tr>
 * <tr><td>"languages"</th><td>String[] { "java", "scala" }</td></tr>
 * </tbody>
 * </table>
 */
@Deprecated
public class IndexedSharedResourceCodingStrategy //extends AbstractRequestTargetUrlCodingStrategy
{
  // resource key of of resource we map to
  //private final String resourceKey;

  /**
   * mount resource with specified key under indexed path
   *
   * @param mountPath   path the resource will be mounted to
   * @param resourceKey key of the resource
   */
//  public IndexedSharedResourceCodingStrategy(String mountPath, String resourceKey)
//  {
//    super(mountPath);
//
//    if (resourceKey == null)
//      throw new IllegalArgumentException("resource key must not be null");
//
//    this.resourceKey = resourceKey;
//  }
//
//  public CharSequence encode(final IRequestTarget requestTarget)
//  {
//    if (!(requestTarget instanceof ISharedResourceRequestTarget))
//      throw new IllegalArgumentException(
//       "This encoder can only be used with instances of " + ISharedResourceRequestTarget.class.getName());
//
//    final ISharedResourceRequestTarget target = (ISharedResourceRequestTarget) requestTarget;
//
//    // create url to shared resource
//    final AppendingStringBuffer url = new AppendingStringBuffer();
//    url.append(getMountPath());
//
//    final RequestParameters requestParameters = target.getRequestParameters();
//
//    Map params = requestParameters.getParameters();
//
//    if (params != null)
//    {
//      params = new HashMap(params);
//
//      int index = 0;
//
//      // append indexed parameters to url:
//      // these parameters are enumerated with the keys "0", "1", ...
//      while (!params.isEmpty())
//      {
//        final String key = Integer.toString(index++);
//        final Object value = params.get(key);
//
//        // no more indexed parameters?
//        if (value == null)
//          break;
//
//        // indexed parameters may not contain arrays
//        if (value instanceof String[])
//          throw new IllegalArgumentException("indexed parameter value must not be an array");
//
//        // remove indexed parameters from rest of parameters
//        params.remove(key);
//
//        // append indexed parameter to url
//        url.append('/').append(urlEncodePathComponent(value.toString()));
//      }
//
//      // create query string from remaining parameters
//      if (!params.isEmpty())
//      {
//        boolean first = true;
//
//        // go through remaining parameters
//        final Iterator itParams = params.entrySet().iterator();
//        while (itParams.hasNext())
//        {
//          final Map.Entry arg = (Map.Entry) itParams.next();
//          final String key = urlEncodeQueryComponent(arg.getKey().toString());
//          final Object obj = arg.getValue();
//
//          // for string arrays, create multiple query string parameters with all the values
//          if (obj instanceof String[])
//          {
//            final String[] values = (String[]) obj;
//            for (int i = 0; i < values.length; i++)
//            {
//              appendToQueryString(url, first, key, values[i]);
//              first = false;
//            }
//          }
//          else
//          {
//            // for single query string value, just append it to url
//            appendToQueryString(url, first, key, obj.toString());
//            first = false;
//          }
//        }
//      }
//    }
//    return url;
//  }
//
//  // helper method
//  private void appendToQueryString(AppendingStringBuffer url, boolean first, final String key, final String value)
//  {
//    url.append(first ? '?' : '&');
//    url.append(key);
//    url.append('=');
//    url.append(urlEncodeQueryComponent(value));
//  }
//
//  public IRequestTarget decode(final RequestParameters requestParameters)
//  {
//    if (requestParameters == null)
//      throw new IllegalArgumentException("request parameters must not be null");
//
//    // get resource path
//    String path = requestParameters.getPath().substring(getMountPath().length());
//
//    // cut away query string
//    int startOfQueryString = path.indexOf("?");
//    if (startOfQueryString != -1)
//      path = path.substring(0, startOfQueryString);
//
//    final ValueMap parameters = decodeParameters(path, requestParameters.getParameters());
//
//    requestParameters.setParameters(parameters);
//    requestParameters.setResourceKey(resourceKey);
//    return new SharedResourceRequestTarget(requestParameters);
//  }
//
//  @Override
//protected ValueMap decodeParameters(String path, Map queryParameters)
//  {
//    final ValueMap parameters = new ValueMap(queryParameters);
//
//    // add indexed parameters to parameters map
//    if (!Strings.isEmpty(path))
//    {
//      final StringTokenizer tokens = new StringTokenizer(path, "/");
//
//      int index = 0;
//      while (tokens.hasMoreTokens())
//        parameters.add(Integer.toString(index++), tokens.nextToken());
//    }
//    return parameters;
//  }
//
//  public boolean matches(final IRequestTarget requestTarget)
//  {
//    if (!(requestTarget instanceof ISharedResourceRequestTarget))
//      return false;
//
//    final ISharedResourceRequestTarget target = (ISharedResourceRequestTarget) requestTarget;
//    return resourceKey.equals(target.getRequestParameters().getResourceKey());
//  }
}
