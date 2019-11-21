package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Wei
 */
@Service
public class SearchService {
    private  final  String solr_url = "http://127.0.0.1:8983/solr/questioncommunity";
    private HttpSolrClient client = new HttpSolrClient.Builder(solr_url).build();
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";
    private static final String QUERY_FIELD = "keyvalue";

    /**
     * 来执行solr的查询命令
     * @param keyWord 查询单词
     * @param hlpre 前缀
     * @param hlpost 后缀
     * @param offset 第几个开始
     * @param count  展示的数目
     * @return  返回的检索值
     * @throws IOException
     * @throws SolrServerException
     */
    public List<Question> search(String keyWord,String hlpre,String hlpost,int offset,int count)
            throws IOException, SolrServerException {
        SolrQuery query =new SolrQuery(keyWord);
        List<Question> reslist =new ArrayList<>();
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlpre);
        query.setHighlightSimplePost(hlpost);
        query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);
        query.set("df",QUERY_FIELD);
        QueryResponse response = client.query(query);
        for(Map.Entry<String,Map<String,List<String>>> entry:response.getHighlighting().entrySet()){
            Question question =new Question();
            question.setId(Integer.parseInt(entry.getKey()));
            if(entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
                List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                if (titleList.size() > 0) {
                    question.setTitle(titleList.get(0));
                }
            }
            if(entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (contentList.size() > 0) {
                    question.setContent(contentList.get(0));
                }
            }
            reslist.add(question);
        }
        return  reslist;
    }
        public boolean indexUpdate(int id,String content,String title) throws IOException, SolrServerException {
            SolrInputDocument doc =new SolrInputDocument();
            doc.setField("id",id);
            doc.setField("question_content",content);
            doc.setField("question_title",title);
            UpdateResponse response = client.add(doc,1000);
            return  response!=null&&response.getStatus()==0;
        }
}
