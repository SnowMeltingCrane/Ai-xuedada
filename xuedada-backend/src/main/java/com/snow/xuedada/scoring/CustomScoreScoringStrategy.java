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
import java.util.List;
import java.util.Optional;

@ScoringStrategyConfig(appType = 0, scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {

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
                        .orderByDesc(ScoringResult::getResultScoreRange)
        );

        // 2.统计用户得分
        int totalScore = 0;
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();


        // 遍历题目列表
        for(QuestionContentDTO questionContentDTO : questionContent){
            // 遍历答案列表
            for(String choice : choices){
                // 遍历题目中的选项
                for(QuestionContentDTO.Option option : questionContentDTO.getOptions()){

                    // 如果答案和选项中的key匹配
                    if(option.getKey().equals(choice)){

                        String result = option.getResult();
                        int score = Optional.of(option.getScore()).orElse(0);
                        totalScore += score;
                    }
                }
            }
        }

        // 3.遍历得分结果，找到对应选项的得分
        ScoringResult maxScoringResult = scoringResultList.get(0);


        for(ScoringResult scoringResult : scoringResultList){
            if(totalScore >= scoringResult.getResultScoreRange()){
                maxScoringResult = scoringResult;
                break;
            }
        }
        // 4.构造对象返回
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        userAnswer.setResultScore(totalScore);

        return userAnswer;
    }
}
