package com.cliptripbe.feature.video.application;

import static com.cliptripbe.global.response.type.ErrorType.CHATGPT_NO_RESPONSE;
import static com.cliptripbe.global.response.type.ErrorType.KAKAO_MAP_NO_RESPONSE;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.feature.video.api.dto.request.ExtractPlaceRequestDto;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.kakao.KakaoMapService;
import com.cliptripbe.infrastructure.openai.application.ChatGPTService;
import com.cliptripbe.infrastructure.openai.prompt.PromptConstants;
import com.cliptripbe.infrastructure.openai.utils.ChatGPTUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final ChatGPTService chatGPTService;
    private final KakaoMapService kakaoMapService;

    public void extractPlace(User user, ExtractPlaceRequestDto request) {
        String caption1 = "전체 자막";

        String caption =
            "음악]\\nYeah.\\n[음악]\\n[음악]\\n우와 교리김밥 10년 년 전 여행\\n왔을 때 교류김밥\\n먹었는데 잘했고 잘하고 있고 잘할\\n거야. 그래.\\n사실 뭐\\n[음악]\\n[음악]\\n[음악]\\n[음악]\\n[음악]\\n[음악]\\n가야겠다.\\n2024년 바는 바이\\n[음악]\\n진짜 쟤네 너무 신기해요. 나무들\\n너네 진짜\\n뭐야?\\n대박이다. 어떻게 저기서 나무가\\n저렇게 잘라는 거지?\\n안녕하세요. 네.\\n아 다 쓸게요.네\\n보이시게 달라\\n[음악]\\n[음악]\\n자가\\n[음악]\\n이제 아니\\n하\\n[음악]\\n4\\n너네 왜 그러고 있어 무섭게?\\n자유 문방한\\n거야. 어 나한테 오는\\n거야? 안\\n돼.\\n무서워. 아니야. 오지 와. 어 여기\\n강아지 왜 이렇게 많은\\n거야? 잘\\n있어. 아 왜 길이 이렇게 갈겨지지?\\n골목기로 가면 안 될 거 같아요. 제\\n강아지 좀 무서워해 가지고.\\n안녕.\\n안녕. 가는 길이 너무\\n예쁩니다. 우와.\\n[박수]\\n여기가 화장치.\\n흠. 냄새도 안 나고 엄청 깨끗해요.\\n치약도\\n있고 당연히 어메니티도 있고.\\n으흠. 아,\\n향기다. 건물은 되게\\n낡았었는데. 그리고 저희\\n방입니다.\\n우와.\\n아늑. 오, 저희 짐도 갖다 놔\\n주셨어요. 엄청 쏙.\\n잠깐\\n들어왔어요. 원래 저는 1인 객실\\n예약하려고 했는데 거기가 예약이 다\\n차 가지고 1인실이 찼을 경우에\\n네버톡으로 문의 드리면 2인실이\\n비었을 경우 여기를 같은 가격에 해\\n주신다고 하더라고요. 그래서 다행히\\n같은 가격에 하게 됐습니다. 어 이거\\n뭐지?\\n네.\\n감사합니다. 지금 경주가 엄청 더운\\n건 아닌데 좀만\\n걸어다녀도 땀이 나기는\\n해요. 좀만 늦게 왔으면 못\\n돌아다녔을 것 같아.\\n저는 일단 조금\\n쉬었다가 아 청배랑 대응원 이런 거\\n봐야 되는데 더워서 못\\n걸어다니겠어요. 좀 이따가\\n나가겠습니다. 조금\\n덥고 저는 좀 눕다가 나가겠습니다.\\n밥 먹으러 가는 길에 하늘이 너무\\n예뻐\\n가지고 이번\\n산물입니다.\\n[음악]\\n[음악]\\n주세요. 세요.\\n[음악]\\n[음악]\\n[음악]\\n[음악]\\n[음악]\\n해가지기 전에 빨리 시내로 넘어가.\\n가야 합니다. 버스가 오고 있어\\n가지고 여기는 너무너무 맛있었어요.\\n내 스타일 근처에 괜찮은 카페가\\n있는데 너무 늦을 거 같아 가지고\\n저는 못 갔는데 혹시 여기 코스로\\n오시면은 그 카페도 한번\\n추천드립니다.\\n뷰가 되게 예쁘대요. 기사님 저 두\\n가지 마세요. 무서워.\\n어쩌면 제가 더 무서울지도 하얀\\n소기씨\\n[음악]\\n거야. 나의 모든 것도 힘들죠.\\n그렇게 수많은 사람들 그중에서\\n[박수]\\n날\\n아침 이제 체크 체크아웃이\\n1한시간 정도 남아서 이제 씻고 나갈\\n준비하면 될 거 같습니다.\\n으차차차차차. 아, 씻는 거 너무\\n귀찮지만 씻어야 합니다. 나름 계획을\\n어제 새벽에 촘촘하게 짰어요.\\n진짜 저는 준비를 바쳤고 머리가 덜\\n말렸어.\\n다가보겠습니다. 주일차\\n[음악]\\n[박수]\\n[음악]\\n[음악]\\n[음악]\\n[음악]\\n[음악]\\n다이소에서 산 양산. 아주\\n무적템입니다. 진작게 살 걸.\\n어제부터 살 걸. 여기가 대응원\\n같은데 입구를 못 찾혔어요. 이쪽\\n내려가면 청성대도 나온다 그래 가지고\\n더 쌈밥집 구글에서 봤어. 저는 쌈밥\\n아 파리 야 오지 마 쌈밥집이 이렇게\\n예쁠 일인가?\\n와 진짜\\n예쁘다. 진짜 경주의 퍼스탈\\n컬러는 초록이다. 여기 어 입구세요?\\n입구예요. 아\\n화장실인가? 어 대원. 어, 여기\\n입구다.\\n오, 얻어 걸렸어요.\\n와우.\\n내가\\n[음악]\\n첨성대\\n왔는데 완전\\n땡겨. 죽는 거\\n아닐까? 그래도\\n왔으니 표준마가 가래서\\n갑니다. 나 말 잘 듣지?\\n와 진짜 땡병. 저 양산 없었으면\\n진짜 죽었을지도 몰라요. 정주는 양산\\n필수예요.\\n아니에다 나와.\\n나\\n[음악]\\n[음악]\\n둘\\n셋 넷\\n다섯 여섯\\n여덟\\n아홉\\n호드야 아까 왜 지었어? 깜짝\\n놀랬잖아 이제는 모른 척한다.\\n여행 오셨어요?\\n어네 너무 예뻐. 속에서 지금 택시\\n타고 오신 거예요? 아, 네. 시간이\\n없어 가지고 택시를\\n탔는데 논밭을지 마시고 나중에 집에\\n갈 수 있나 이런 생각이 좀 들어\\n이\\n[음악]\\n너시나 기도\\n[음악]\\n리가 나이가\\n[음악]\\n[음악]\\n누군가의 책방에서서 책 뜯어\\n볼게요. 저는\\n책을 주건 사\\n왔어요. 이거는 생각이 많은 아\\n생각이 방안을 돌아다녀. 이거는 이제\\n책\\n구경하다가이 책이 좀 특이한 거예요.\\n그 보니까는 작가님이 간네 수공업으로\\n만든\\n책이래요. 직접 만드신 책.\\n그래가지고이 시리즈가 되게\\n많았는데 약간 다른 책들보다 약간\\n책에 대한 작가님의 애정에 좀 많이\\n담긴 거 같아서이 책을 너무 갖고\\n싶더라고요. 가격도 한 6,000원\\n정도. 그리고 제목도 약간 제 마음을\\n끌었어요. 저도 진짜 생각이 많은\\n사람이거든요. 잡생각도 많고 상상도\\n많이 하고 딱 제 얘기하는 거 같아\\n가지고이 책이 갖고 싶어서이 책도\\n구매를 했습니다. 어떻게 이렇게 책을\\n만드셨을까요? 진짜 대단하신 거\\n같아요. 이것도 생각이 방안을\\n돌아다녀. 무슨 생각? 이런저런\\n생각. 내 생각은 그러니까 전화했지.\\n어머.\\n신쿵하네. 음.\\n좋은 거\\n같아요. 멋있습니다. 작가님. 그리고\\n제가 꼭 가면 사오는\\n시크릿북.이 글기도 딱 제 얘기 같아\\n가지고이 책이 갖고 싶었거든요. 어떤\\n책인지 본 거\\n같아.\\n음. 다정하기 싫어서. 다정하게.\\n제가 요즘 되게 좋아하는 단어거든요.\\n다정하기. 저의 인생 목표입니다.\\n다정한 사람이 되는 거. 오,\\n김현님. 음. 처음 보는 작가님인데.\\n[음악]\\n이것도 되게 가볍게 잘 읽을 수 있을\\n것 같아요. 제가 그 여행 가서 꼭\\n이렇게 시크릿북을 사거나 책을 사는\\n이유는 되게 좋은 공간을 이렇게\\n만들어 주신 사장님에 대한 감사함을\\n표시. 제가 뭐라고? 좀 더 그 첫번\\n운영에 도움이 됐으면 좋겠다라는\\n생각이 들어서 책을 사는 것도 있고\\n그리고 원래 책을 좋아하니까 사는\\n것도 있고 여행 기념품으로도 책만한게\\n없는 거 같기도 하더라고요. 그래서\\n사는 것도 있고 또 하나는 그\\n시크릿북을 저희가 되게 좋아하는데\\n그래서 시크릿북을 좋아하는 이유는\\n예전에 강릉에 한낮재 바다라는\\n독립서점이 있어요. 거기서 제가 아마\\n처음으로 시크릿북을 샀던 거 같은데\\n뜯어 보니까 제가 한 번도 뵙지 못한\\n작가님의 S 집이었어요. 그게 신유진\\n작가님의 이제 저기도 있는데 여긴\\n저의 거실 책정인데 제가 처음 봤던게\\n15번의 밤 신유지 작가님\\n책이었어요. 그게 시크릿북이었는데\\n저는 보통 교부문고에서 책을 구매하면\\n사실 소설 위주의 책을 되게 좋아해서\\n소설만 주로 사는 편이고 그리고 제가\\n좋아하는 작가님들 있어요. 그 작가님\\n위주로 조금 많이 사는 편이어서\\n어쩌면은 제가 그 시크릿북이\\n아니었다면 이렇게 좋은 작가님의 책을\\n만나 볼 수 없지 않았을까? 그래서\\n되게 그 경험이 좋았거든요. 그때부터\\n제가 독립서점을 다니고 거기서 파는\\n거기만의 이제 독립 출판물을 사는게\\n제 여행의 약간 필스 코스가 됐어요.\\n그래 가지고 이번에도\\n김현 작가님 그리고 김종환\\n작가님이었던 거 같은데 좋은\\n작가님들의 책을 만날 수 있어서\\n너무너무 좋습니다. 잘 읽어\\n보겠습니다. 좋은 책 감사합니다.\\n책 언박싱 끝.\"\n"
                + "}";

        String requestPrompt =
            PromptConstants.EXTRACT_CAPTION + System.lineSeparator() + caption;

        List<String> extractPlaces = chatGPTService.ask(requestPrompt)
            .subscribeOn(Schedulers.boundedElastic())
            .map(ChatGPTUtils::extractPlaces)
            .blockOptional()
            .orElseThrow(() -> new CustomException(CHATGPT_NO_RESPONSE));

        List<PlaceDto> places = kakaoMapService.searchFirstPlaces(extractPlaces)
            .subscribeOn(Schedulers.boundedElastic())
            .blockOptional()
            .orElseThrow(() -> new CustomException(KAKAO_MAP_NO_RESPONSE));

//        Mono<List<PlaceDto>> places = kakaoMapService.searchPlaces("경주 독립서점");
//        for (PlaceDto place : places.block()) {
//            System.out.println(place.placeName());
//            System.out.println(place.roadAddress());
//            System.out.println("---------------");
//        }

        System.out.println("해위~~");
//        List<PlaceDto> allPlaces = extractPlaces.stream()
//            .map(kakaoMapService::searchPlaces)
//            .flatMap(List::stream)
//            .collect(Collectors.toList());

    }
}