<template>
  <div>
    <label for="hashtag">해시태그</label>
    <input name='hashtag' id="hashtag" pattern='^[A-Za-z_✲ \u3131-\u314E\u314F-\u3163\uAC00-\uD7A3]{1,15}$'>
  </div>
</template>
  
<script setup>
import { onMounted, ref } from 'vue';
import Tagify from "@yaireo/tagify";

const emit = defineEmits(['updateHashtags']);
const hashtags = ref([]);

onMounted(() => {
  const hashtagInput = document.getElementById("hashtag");
  const tagify = new Tagify(hashtagInput, {
    pattern: /^[A-Za-z_✲ \u3131-\u314E\u314F-\u3163\uAC00-\uD7A3]{1,15}$/,
    // Validate typed tag(s) by Regex. Here maximum chars length is defined as "20"
    delimiters: ",| ",        // add new tags when a comma or a space character is entered
    trim: false,        // if "delimiters" setting is using space as a delimeter, then "trim" should be set to "false"
    keepInvalidTags: true,         // do not remove invalid tags (but keep them marked as invalid)
    // createInvalidTags: false,
    editTags: {
      clicks: 2,              // single click to edit a tag
      keepInvalid: false      // if after editing, tag is invalid, auto-revert
    },
    maxTags: 6,
    blacklist: ["foo", "bar", "baz"],
    whitelist: ["temple", "stun", "detective", "sign", "passion", "routine", "deck", "discriminate", "relaxation", "fraud", "attractive", "soft", "forecast", "point", "thank", "stage", "eliminate", "effective", "flood", "passive", "skilled", "separation", "contact", "compromise", "reality", "district", "nationalist", "leg", "porter", "conviction", "worker", "vegetable", "commerce", "conception", "particle", "honor", "stick", "tail", "pumpkin", "core", "mouse", "egg", "population", "unique", "behavior", "onion", "disaster", "cute", "pipe", "sock", "dialect", "horse", "swear", "owner", "cope", "global", "improvement", "artist", "shed", "constant", "bond", "brink", "shower", "spot", "inject", "bowel", "homosexual", "trust", "exclude", "tough", "sickness", "prevalence", "sister", "resolution", "cattle", "cultural", "innocent", "burial", "bundle", "thaw", "respectable", "thirsty", "exposure", "team", "creed", "facade", "calendar", "filter", "utter", "dominate", "predator", "discover", "theorist", "hospitality", "damage", "woman", "rub", "crop", "unpleasant", "halt", "inch", "birthday", "lack", "throne", "maximum", "pause", "digress", "fossil", "policy", "instrument", "trunk", "frame", "measure", "hall", "support", "convenience", "house", "partnership", "inspector", "looting", "ranch", "asset", "rally", "explicit", "leak", "monarch", "ethics", "applied", "aviation", "dentist", "great", "ethnic", "sodium", "truth", "constellation", "lease", "guide", "break", "conclusion", "button", "recording", "horizon", "council", "paradox", "bride", "weigh", "like", "noble", "transition", "accumulation", "arrow", "stitch", "academy", "glimpse", "case", "researcher", "constitutional", "notion", "bathroom", "revolutionary", "soldier", "vehicle", "betray", "gear", "pan", "quarter", "embarrassment", "golf", "shark", "constitution", "club", "college", "duty", "eaux", "know", "collection", "burst", "fun", "animal", "expectation", "persist", "insure", "tick", "account", "initiative", "tourist", "member", "example", "plant", "river", "ratio", "view", "coast", "latest", "invite", "help", "falsify", "allocation", "degree", "feel", "resort", "means", "excuse", "injury", "pupil", "shaft", "allow", "ton", "tube", "dress", "speaker", "double", "theater", "opposed", "holiday", "screw", "cutting", "picture", "laborer", "conservation", "kneel", "miracle", "brand", "nomination", "characteristic", "referral", "carbon", "valley", "hot", "climb", "wrestle", "motorist", "update", "loot", "mosquito", "delivery", "eagle", "guideline", "hurt", "feedback", "finish", "traffic", "competence", "serve", "archive", "feeling", "hope", "seal", "ear", "oven", "vote", "ballot", "study", "negative", "declaration", "particular", "pattern", "suburb", "intervention", "brake", "frequency", "drink", "affair", "contemporary", "prince", "dry", "mole", "lazy", "undermine", "radio", "legislation", "circumstance", "bear", "left", "pony", "industry", "mastermind", "criticism", "sheep", "failure", "chain", "depressed", "launch", "script", "green", "weave", "please", "surprise", "doctor", "revive", "banquet", "belong", "correction", "door", "image", "integrity", "intermediate", "sense", "formal", "cane", "gloom", "toast", "pension", "exception", "prey", "random", "nose", "predict", "needle", "satisfaction", "establish", "fit", "vigorous", "urgency", "X-ray", "equinox", "variety", "proclaim", "conceive", "bulb", "vegetarian", "available", "stake", "publicity", "strikebreaker", "portrait", "sink", "frog", "ruin", "studio", "match", "electron", "captain", "channel", "navy", "set", "recommend", "appoint", "liberal", "missile", "sample", "result", "poor", "efflux", "glance", "timetable", "advertise", "personality", "aunt", "dog"],
    transformTag: transformTag,
    backspace: "edit",
    placeholder: "해시태그를 입력하세요",
    dropdown: {
      enabled: 1,            // show suggestion after 1 typed character
      fuzzySearch: false,    // match only suggestions that starts with the typed characters
      position: 'text',      // position suggestions list next to typed text
      caseSensitive: true,   // allow adding duplicate items if their case is different
    },
    templates: {
      dropdownItemNoMatch: function (data) {
        return `<div class='${this.settings.classNames.dropdownItem}' value="noMatch" tabindex="0" role="option">
                      No suggestion found for: <strong>${data.value}</strong>
                  </div>`
      }
    },
    callbacks: {
      add: onTagAdd,
      remove: onTagRemove
    }
  });

  function onTagAdd(e) {
    hashtags.value.push(e.detail.data.value);
    emit('updateHashtags', hashtags.value);
  }

  function onTagRemove(e) {
    const index = hashtags.value.findIndex(tag => tag.value === e.detail.data.value);
    if (index !== -1) {
      hashtags.value.splice(index, 1);
      emit('updateHashtags', hashtags.value);
    }
  }

  function getRandomColor() {
    const h = Math.floor(Math.random() * 360);
    const s = Math.floor(Math.random() * (70 - 40 + 1)) + 40;
    const l = Math.floor(Math.random() * (72 - 65 + 1)) + 65;
    return `hsl(${h}, ${s}%, ${l}%)`;
  }

  function transformTag(tagData) {
    tagData.color = getRandomColor();
    tagData.style = `--tag-bg: ${tagData.color}`;
    if (tagData.value.toLowerCase() === 'shit') {
      tagData.value = 's✲✲t';
    }
  }

  tagify.on('add', function (e) {
    console.log(e.detail);
  });

  tagify.on('invalid', function (e) {
    console.log(e, e.detail);
  });

  let clickDebounce;

  tagify.on('click', function (e) {
    const { tag: tagElm, data: tagData } = e.detail;

    clearTimeout(clickDebounce);
    clickDebounce = setTimeout(() => {
      tagData.color = getRandomColor();
      tagData.style = `--tag-bg: ${tagData.color}`;
      tagify.replaceTag(tagElm, tagData);
    }, 200);
  });

  tagify.on('dblclick', function (e) {
    clearTimeout(clickDebounce);
  });
});
</script>
  
<style scoped>
@import "https://unpkg.com/@yaireo/tagify/dist/tagify.css";

.advance .tagify__tag {
  --tag-hover: var(--tag-bg);
}
</style>
  
  
  
  
  
  