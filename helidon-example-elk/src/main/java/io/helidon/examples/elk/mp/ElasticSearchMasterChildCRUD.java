package io.helidon.examples.elk.mp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ElasticSearchMasterChildCRUD {
    //The config parameters for the connection
    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";

    private  RestHighLevelClient restHighLevelClient;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private Client client;
    private static final String INDEX = "hr";
    private static final String TYPE = "hrdoc";

    public ElasticSearchMasterChildCRUD() throws UnknownHostException {

        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(
                        new TransportAddress(InetAddress.getByName("localhost"), 9300));
        if (restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(HOST, PORT_ONE, SCHEME),
                            new HttpHost(HOST, PORT_TWO, SCHEME)));
        }


    }

    /**
     * Implemented Singleton pattern here
     * so that there is just one connection at a time.
     *
     * @return RestHighLevelClient
     */
    public synchronized RestHighLevelClient makeConnection() {

        if (restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(HOST, PORT_ONE, SCHEME),
                            new HttpHost(HOST, PORT_TWO, SCHEME)));
        }

        return restHighLevelClient;
    }
    public void createIndex()
    {
        IndicesExistsResponse exists = client.admin().indices().exists(new IndicesExistsRequest(INDEX)).actionGet();
        if(exists.isExists()) {
           return;
        }

        client.admin().indices().prepareCreate(INDEX).execute().actionGet();
    }
    public void putMappings() throws IOException, ExecutionException, InterruptedException {
/**
 *   "my_join_field": {
 *           "type": "join",
 *           "relations": {
 *             "question": "answer"
 *           }
 */
        XContentBuilder hrBuilder = XContentFactory.jsonBuilder()
                .startObject()
                    .startObject("mappings")
                        .startObject("hrdoc")
                            .startObject("properties")
                                .startObject("id")
                                    .field("type", "keyword")
                                    .field("index", false)
                                .endObject()
                                .startObject("departmentName")
                                    .field("type", "text")
                                .endObject()
                                .startObject("location")
                                    .field("type", "text")
                                .endObject()
                                .startObject("firstName")
                                    .field("type", "text")
                                .endObject()
                                .startObject("lastName")
                                    .field("type", "text")
                                .endObject()
                                .startObject("sex")
                                    .field("type", "keyword")
                                .endObject()
                                .startObject("relationType")
                                    .field("type", "join")
                                    .field("eager_global_ordinals", true)
                                    .startObject("relations")
                                        .field("department", "employee")
                                    .endObject()
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject();
        System.out.println( "hrdoc: "+Strings.toString(hrBuilder.prettyPrint()));
        CreateIndexRequest request = new CreateIndexRequest(INDEX);
        request.source(hrBuilder);
        AcknowledgedResponse reponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);


        //client.admin().indices().preparePutMapping(INDEX).setSource(hrBuilder).execute().actionGet();

    }

    public synchronized void closeConnection() throws IOException {
        restHighLevelClient.close();
        restHighLevelClient = null;
    }

    public IndexResponse insertDepartment(HRDoc dept) throws IOException {

            IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, dept.getId());
            indexRequest.source(new ObjectMapper().writeValueAsString(dept), XContentType.JSON);
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            return indexResponse;

    }
    public IndexResponse insertEmployee(HRDoc dept, String route) throws IOException {
         IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, dept.getEmployeeId());
        indexRequest.source(new ObjectMapper().writeValueAsString(dept), XContentType.JSON);
        indexRequest.routing(route);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        return indexResponse;

    }

    public UpdateResponse updateEmployee(HRDoc emp, String route) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, emp.getEmployeeId())
                .fetchSource(true);    // Fetch Object after its update
        updateRequest.routing(route);
        String deptJson = objectMapper.writeValueAsString(emp);
        updateRequest.doc(deptJson, XContentType.JSON);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println("updateResponse "+updateResponse );
        return updateResponse;

    }

    private  Employee createEmployee(String firstName, String lastName, String sex) {

        Employee emp = new Employee(UUID.randomUUID().toString(), firstName, lastName, sex);
        return emp;
    }

    public List<HRDoc> findDepartmentsByName(String name) throws IOException {
        List<HRDoc> depts = new ArrayList<>();
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", name)
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(2)
                .maxExpansions(10);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(5);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            HRDoc dept = new ObjectMapper().readValue(searchHit.getSourceAsString(), HRDoc.class);
            depts.add(dept);
        }
        return depts;
    }

    public List<HRDoc> findAllDepartments() throws IOException {
        List<HRDoc> depts = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(5);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("findAllDepartments:: " +searchResponse );
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            HRDoc dept = new ObjectMapper().readValue(searchHit.getSourceAsString(), HRDoc.class);
            depts.add(dept);
        }
        return depts;
    }
    public List<HRDoc> findAllEmployeesByName(String employeeName) throws IOException {
        List<HRDoc> depts = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery( employeeName,
                "firstName", "lastName" ));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("findAllDepartments:: " +searchResponse );
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            HRDoc dept = new ObjectMapper().readValue(searchHit.getSourceAsString(), HRDoc.class);
            depts.add(dept);
        }
        return depts;
    }
    public static void instertDoc(ElasticSearchMasterChildCRUD elasticSearchCRUD) throws InterruptedException, ExecutionException, IOException {
        //elasticSearchCRUD.createIndex();
        elasticSearchCRUD.putMappings();
        List<HRDoc> depts = elasticSearchCRUD.findDepartmentsByName("IT");
        if (depts.isEmpty()) {
            HRDoc dept1 = new HRDoc();
            dept1.setId(UUID.randomUUID().toString());
            dept1.setDepartmentName("IT");
            dept1.setLocation("FC");
            dept1.setRelationType(new HRDoc.RelationType("department",null) );
            IndexResponse response1 = elasticSearchCRUD.insertDepartment(dept1);
            System.out.println(response1);

            HRDoc dept2 = new HRDoc();
            dept2.setId(UUID.randomUUID().toString());
            dept2.setDepartmentName("HR");
            dept2.setLocation("FC");
            dept2.setRelationType(new HRDoc.RelationType("department",null) );
            IndexResponse response2 = elasticSearchCRUD.insertDepartment(dept2);
            System.out.println(response2);
            for(int i=0;i<4;i++) {
                HRDoc emp = new HRDoc();
                emp.setEmployeeId(UUID.randomUUID().toString());
                emp.setFirstName("Jobinesh" + emp.getEmployeeId());
                emp.setLastName("Purushothaman" + emp.getEmployeeId());
                emp.setSex("MALE");
                emp.setRelationType(new HRDoc.RelationType("employee", dept1.getId()));
                elasticSearchCRUD.insertEmployee(emp, dept1.getId());
                //emp.setLastName("Manakkattil");
                // elasticSearchCRUD.updateEmployee(emp, dept.getId());
            }
            for(int i=0;i<3;i++) {
                HRDoc emp = new HRDoc();
                emp.setEmployeeId(UUID.randomUUID().toString());
                emp.setFirstName("Remys" + emp.getEmployeeId());
                emp.setLastName("Jobinesh");
                emp.setSex("FEMALE");
                emp.setRelationType(new HRDoc.RelationType("employee", dept2.getId()));
                elasticSearchCRUD.insertEmployee(emp, dept2.getId());
                //emp.setLastName("JP");
                //elasticSearchCRUD.updateEmployee(emp, dept.getId());
            }
        }
    }
    public static void main(String[] args)  {
        try {
            ElasticSearchMasterChildCRUD elasticSearchCRUD = new ElasticSearchMasterChildCRUD();
            elasticSearchCRUD.makeConnection();

            List<HRDoc> depts  = elasticSearchCRUD.findAllDepartments();
            System.out.println("Depts read from elk " + depts);
            if(depts.size()==0){
                instertDoc(elasticSearchCRUD);
            }else{
                List<HRDoc> emps  = elasticSearchCRUD.findAllEmployeesByName("Jobinesh");
                System.out.println("Emps read from elk " + emps);
            }
            elasticSearchCRUD.closeConnection();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
