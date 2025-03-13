package com.snow.xuedada.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.snow.xuedada.model.dto.question.QuestionContentDTO;
import com.snow.xuedada.model.entity.App;
import com.snow.xuedada.model.entity.Question;
import com.snow.xuedada.model.entity.ScoringResult;
import com.snow.xuedada.model.entity.UserAnswer;
import com.snow.xuedada.model.vo.QuestionVO;

import com.snow.xuedada.service.QuestionService;
import com.snow.xuedada.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义测评类应用评分策略
 */
@ScoringStrategyConfig(appType = 1, scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {
    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Long appId = app.getId();

        // 1.根据id查询题目和题目结果信息

        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class)
                        .eq(Question::getAppId, appId)
        );

        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, appId)
        );


        // 2.统计用户每个选择对应的属性个数
        Map<String, Integer> optionCount = new HashMap<>();

        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();


        //遍历题目列表
        for(QuestionContentDTO questionContentDTO : questionContent){
            //遍历答案列表
            for(String choice : choices){
                //遍历题目中的选项
                for(QuestionContentDTO.Option option : questionContentDTO.getOptions()){
                    //如果答案和选项中的key匹配
                    if(option.getKey().equals(choice)){
                        String result = option.getResult();

                        if(!optionCount.containsKey(result)){
                            optionCount.put(result, 0);
                        }

                        optionCount.put(result, optionCount.get(result) + 1);
                    }
                }
            }
        }

        // 3.计算用户每个属性的得分
        int maxScore = 0;
        ScoringResult maxScoringResult = scoringResultList.get(0);

        for(ScoringResult scoringResult : scoringResultList){
            // 计算当前评分结果分数
            List<String> resultProp = JSONUtil.toList(scoringResult.getResultProp(),String.class);

            int score = resultProp.stream()
                    .mapToInt(prop -> optionCount.getOrDefault(prop, 0) )
                    .sum();

            if(score > maxScore) {
                maxScore = score;
                maxScoringResult = scoringResult;
            }
        }


        // 4.计算用户总得分，构造返回值
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());

        return userAnswer;
    }
}
