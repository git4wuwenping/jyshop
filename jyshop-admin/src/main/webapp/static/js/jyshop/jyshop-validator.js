(function ($) {
    $.fn.extend({
        "enableJYSHOPValid": function () {
            $(this).find("input").each(function () {
                if($(this).type!="button" && this.hasAttribute("validateStyle")){
                    var _styles = $(this).attr("validateStyle");
                    _addInputEvent(_styles,this);
                }
            });
        },
        "validateJYSHOP": function () {
            var _checkWrongCount = 0;
            var _focusEle;
            $(this).find("input").each(function () {
                if($(this).type!="button" && this.hasAttribute("validateStyle")){
                    var _styles = $(this).attr("validateStyle");
                    if(!_checkInput(_styles,this)){
                        _checkWrongCount++;
                        if( _checkWrongCount == 1 ){
                            _focusEle = this;
                        }
                    }
                }
            });
            if(_checkWrongCount == 0){
                return true;
            }
            $(_focusEle).focus();
            return false;
        }
    });

    function _addInputEvent(_styles, ele){
        $(ele).bind("input propertychange change",function(event){
            _checkInput(_styles, ele);
        });
    }

    function _checkInput(_styles, ele){
        var _styleArr = $.trim(_styles).split(";");
        var _wrongFlag = false;
        $.each(_styleArr, function (index, _style) {
            var _flg = false;
            _style = $.trim(_style);
            switch (validStr(_style))
            {
                case "notEmpty" :
                    _flg = _checkNotEmpty(ele); //不为空
                    break;
                case "length" :
                    _flg = _checkLength(ele,_style);//长度 不大于x
                    break;
                case "lengthSE" :
                    _flg = _checkLengthSE(ele,_style);//长度 x-x
                    break;
                case "creditCard" :
                    _flg = _checkCreditCard(ele);//信用卡
                    break;
                case "date" :
                    _flg = _checkDate(ele,_style);//日期
                    break;
                case "emailAddress" :
                    _flg = _checkEmailAddress(ele);//email地址
                    break;
                case "file" :
                    _flg = _checkFile(ele);//文件
                    break;
                case "url" :
                    _flg = _checkUrl(ele);//uri
                    break;
                case "greaterThan" :
                    _flg = _checkGreaterThan(ele,_style);//不小于xxx
                    break;
                case "lessThan" :
                    _flg = _checkLessThan(ele,_style);//不大于xxx
                    break;
                case "idCard" :
                    _flg = _checkIdCard(ele);//身份证号码
                    break;
                case "tel" :
                    _flg = _checkTel(ele);//电话号码
                    break;
                case "mobile" :
                    _flg = _checkMobile(ele);//手机号码
                    break;
                case "pInteger" :
                    _flg = _checkPInteger(ele);//正整数
                    break;
                case "nInteger" :
                    _flg = _checkNInteger(ele);//负整数
                    break;
                case "integer" :
                    _flg = _checkInteger(ele);//整数
                    break;
                case "pDecimal" :
                    _flg = _checkPDecimal(ele,_style);//正浮点数
                    break;
                case "nDecimal" :
                    _flg = _checkNDecimal(ele,_style);//负浮点数
                    break;
                case "decimal" :
                    _flg = _checkDecimal(ele,_style);//浮点数
                    break;
                case "digits" :
                    _flg = _checkDigits(ele);//数字
                    break;
                default:
                    break;
            }

            if(_flg){
                _wrongFlag = true;
                return false;
            }
        });
        if(!_wrongFlag){
            _removeValidateInfoAfterObject(ele);
            return true;
        }
        return false;
    }

    /**
     * 验证为空
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkNotEmpty(ele){
        var _val = $(ele).prop("value");
        if(_val == null || $.trim(_val) == "" ){
            _writeValidateInfoAfterObject("不能为空",ele);
            return true;
        }
        return false;
    }

    /**
     * 验证长度
     * @param ele
     * @param _style
     * @returns {boolean}
     * @private
     */
    function _checkLength(ele,_style){
        var _val = $(ele).prop("value").trim();
        var maxLen = parseInt( _style.substr(_style.indexOf("(")+1,_style.indexOf(")")-_style.indexOf("(")-1));
        if( _val.length > maxLen ){
            _writeValidateInfoAfterObject("长度大于"+maxLen,ele);
            return true;
        }
        return false;
    }

    /**
     * 验证长度
     * @param ele
     * @param _style
     * @returns {boolean}
     * @private
     */
    function _checkLengthSE(ele,_style){
        var _val = $(ele).prop("value").trim();
        var _len = _style.substr(_style.indexOf("(")+1,_style.indexOf(")")-_style.indexOf("(")-1);
        var _minLen = parseInt( _len.split(",")[0]);
        var _maxLen = parseInt( _len.split(",")[1]);
        if( _val.length < _minLen ){
            _writeValidateInfoAfterObject("长度小于"+_minLen,ele);
            return true;
        }else if( _val.length > _maxLen ){
            _writeValidateInfoAfterObject("长度大于"+_maxLen,ele);
            return true;
        }
        return false;
    }
    /**
     * 验证是否信用卡
     * @param ele
     * @param _style
     * @returns {boolean}
     * @private
     */
    function _checkCreditCard(ele){
        var _val = $(ele).prop("value").trim();
        if (_val === '') {
            _writeValidateInfoAfterObject("请输入正确的信用卡",ele);
            return true;
        }

        // Accept only digits, dashes or spaces
        if (/[^0-9-\s]+/.test(_val)) {
            _writeValidateInfoAfterObject("请输入正确的信用卡",ele);
            return true;
        }
        _val = _val.replace(/\D/g, '');

        if (!luhn(_val)) {
            _writeValidateInfoAfterObject("请输入正确的信用卡",ele);
            return true;
        }

        // Validate the card number based on prefix (IIN ranges) and length
        var cards = {
            AMERICAN_EXPRESS: {
                length: [15],
                prefix: ['34', '37']
            },
            DINERS_CLUB: {
                length: [14],
                prefix: ['300', '301', '302', '303', '304', '305', '36']
            },
            DINERS_CLUB_US: {
                length: [16],
                prefix: ['54', '55']
            },
            DISCOVER: {
                length: [16],
                prefix: ['6011', '622126', '622127', '622128', '622129', '62213',
                    '62214', '62215', '62216', '62217', '62218', '62219',
                    '6222', '6223', '6224', '6225', '6226', '6227', '6228',
                    '62290', '62291', '622920', '622921', '622922', '622923',
                    '622924', '622925', '644', '645', '646', '647', '648',
                    '649', '65']
            },
            JCB: {
                length: [16],
                prefix: ['3528', '3529', '353', '354', '355', '356', '357', '358']
            },
            LASER: {
                length: [16, 17, 18, 19],
                prefix: ['6304', '6706', '6771', '6709']
            },
            MAESTRO: {
                length: [12, 13, 14, 15, 16, 17, 18, 19],
                prefix: ['5018', '5020', '5038', '6304', '6759', '6761', '6762', '6763', '6764', '6765', '6766']
            },
            MASTERCARD: {
                length: [16],
                prefix: ['51', '52', '53', '54', '55']
            },
            SOLO: {
                length: [16, 18, 19],
                prefix: ['6334', '6767']
            },
            UNIONPAY: {
                length: [16, 17, 18, 19],
                prefix: ['622126', '622127', '622128', '622129', '62213', '62214',
                    '62215', '62216', '62217', '62218', '62219', '6222', '6223',
                    '6224', '6225', '6226', '6227', '6228', '62290', '62291',
                    '622920', '622921', '622922', '622923', '622924', '622925']
            },
            VISA: {
                length: [16],
                prefix: ['4']
            }
        };

        var type, i;
        for (type in cards) {
            for (i in cards[type].prefix) {
                if (value.substr(0, cards[type].prefix[i].length) === cards[type].prefix[i]     // Check the prefix
                    && $.inArray(value.length, cards[type].length) !== -1)                      // and length
                {
                    return false;
                }
            }
        }
        _writeValidateInfoAfterObject("请输入正确的信用卡",ele);
        return true;
    }

    /**
     * 验证日期
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkDate(ele,_style){
        var _val = $(ele).prop("value").trim();
        var _reg = _style.substr(_style.indexOf("(")+1,_style.indexOf(")")-_style.indexOf("(")-1).toLowerCase();
        var _regex ;
        switch (_reg) {
            case "yyyymmdd" :
                _regex = /^(\d{4})(0\d{1}|1[0-2])(0\d{1}|[12]\d{1}|3[01])$/;
                break;
            case "yyyy/mm/dd" :
                _regex = /^(\d{4})\/(0\d{1}|1[0-2])\/(0\d{1}|[12]\d{1}|3[01])$/;
                break;
            case "yyyy-mm-dd" :
                _regex = /^(\d{4})-(0\d{1}|1[0-2])-(0\d{1}|[12]\d{1}|3[01])$/;
                break;
            case "yyyymm" :
                _regex = /^(\d{4})(0\d{1}|1[0-2])$/;
                break;
            case "yyyy" :
                _regex = /^(\d{4})$/;
                break;
            case "mmdd" :
                _regex = /^(0\d{1}|1[0-2])(0\d{1}|[12]\d{1}|3[01])$/;
                break;
            case "hh:mm:ss" :
                _regex = /^(0\d{1}|1\d{1}|2[0-3]):[0-5]\d{1}:([0-5]\d{1})$/;
                break;
            case "yyyy-mm-dd hh:mm:ss" :
                _regex = /^(\d{4})-(0\d{1}|1[0-2])-(0\d{1}|[12]\d{1}|3[01])\s+(0\d{1}|1\d{1}|2[0-3]):[0-5]\d{1}:([0-5]\d{1})$/;
                break;
            default:
                break;
        }
        if( !_regex.test(_val) ){
            _writeValidateInfoAfterObject("不正确的日期格式",ele);
            return true;
        }
        return false;
    }

    /**
     * 验证电子邮箱
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkEmailAddress(ele){
        var _val = $(ele).prop("value").trim();
        var _regex = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/; ;

        if( !_regex.test(_val) ){
            _writeValidateInfoAfterObject("不正确的邮件格式",ele);
            return true;
        }
        return false;
    }
    /**
     * 验证文件
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkFile(ele){
        return false;
    }

    /**
     * 验证URL
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkUrl(ele){
        var _val = $(ele).prop("value").trim();
        var _regexStr =  '^((https|http|ftp|rtsp|mms)?://)'
            + '?(([0-9a-z_!~*\'().&=+$%-]+: )?[0-9a-z_!~*\'().&=+$%-]+@)?' //ftp的user@
            + '(([0-9]{1,3}.){3}[0-9]{1,3}' // IP形式的URL- 199.194.52.184
            + '|' // 允许IP和DOMAIN（域名）
            + '([0-9a-z_!~*\'()-]+.)*' // 域名- www.
            + '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z].' // 二级域名
            + '[a-z]{2,6})' // first level domain- .com or .museum
            + '(:[0-9]{1,4})?' // 端口- :80
            + '((/?)|' // a slash isn't required if there is no file name
            + '(/[0-9a-z_!~*\'().;?:@&=+$,%#-]+)+/?)$';
        var re=new RegExp(_regexStr);
        if( !re.test(_val) ){
            _writeValidateInfoAfterObject("不正确的URL格式",ele);
            return true;
        }
        return false;
    }

    /**
     * 验证不小于
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkGreaterThan(ele,_style){
        var _val = $(ele).prop("value").trim();
        var _reg = parseFloat(_style.substr(_style.indexOf("(")+1,_style.indexOf(")")-_style.indexOf("(")-1));

        if( _val <= _reg ){
            _writeValidateInfoAfterObject("输入的值不能小于"+_reg,ele);
            return true;
        }
        return false;
    }
    /**
     * 验证不大于
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkLessThan(ele,_style){
        var _val = $(ele).prop("value").trim();
        var _reg = parseFloat(_style.substr(_style.indexOf("(")+1,_style.indexOf(")")-_style.indexOf("(")-1));

        if( _val >= _reg ){
            _writeValidateInfoAfterObject("输入的值不能大于"+_reg,ele);
            return true;
        }
        return false;
    }

    /**
     * 验证身份证
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkIdCard(ele){
        var _val = $(ele).prop("value").trim();
        var _regex =  /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/;
        if( !_regex.test(_val) ){
            _writeValidateInfoAfterObject("不正确的身份证格式",ele);
            return true;
        }
        return false;
    }

    /**
     * 验证电话号码
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkTel(ele){
        var _val = $(ele).prop("value").trim();
        var _regex = /^0\d{2,3}-?\d{7,8}$/;
        if( !_regex.test(_val) ){
            _writeValidateInfoAfterObject("不正确的电话格式",ele);
            return true;
        }
        return false;
    }

    /**
     * 验证手机号码
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkMobile(ele){
        var _val = $(ele).prop("value").trim();
        var _regex = /^1[3|5|8]\d{9}$/;
        if( !_regex.test(_val) ){
            _writeValidateInfoAfterObject("不正确的手机号码格式",ele);
            return true;
        }
        return false;
    }

    /**
     * 验证正整数
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkPInteger(ele){
        var _val = $(ele).prop("value").trim();
        var _regex =  /^\\d+$/ ;
        if( !_regex.test(_val) ){
            _writeValidateInfoAfterObject("请输入正整数",ele);
            return true;
        }
        return false;
    }

    /**
     * 验证负整数
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkNInteger(ele){
        var _val = $(ele).prop("value").trim();
        var _regex =  /^((-\\d+)|(0+))$/ ;
        if( !_regex.test(_val) ){
            _writeValidateInfoAfterObject("请输入负整数",ele);
            return true;
        }
        return false;
    }
    /**
     * 验证整数
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkInteger(ele){
        var _val = $(ele).prop("value").trim();
        var _regex =  /^-?\\d+$/ ;
        if( !_regex.test(_val) ){
            _writeValidateInfoAfterObject("请输入整数",ele);
            return true;
        }
        return false;
    }

    /**
     * 验证正浮点数
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkPDecimal(ele){
        return false;
    }

    /**
     * 验证负浮点数
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkNDecimal(ele){
        return false;
    }

    /**
     * 验证浮点数
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkDecimal(ele){
        return false;
    }
    /**
     * 验证数字
     * @param ele
     * @returns {boolean}
     * @private
     */
    function _checkDigits(ele){
        return false;
    }

    function _writeValidateInfoAfterObject(info, thisObj) {
        var infoDiv = "<font color='red'>"+info+"</font>"
        var validateInfoObj = thisObj.nextSibling;
        if($(validateInfoObj).hasClass("help-block")) {
            validateInfoObj.innerHTML = infoDiv;
        } else{
            thisObj.insertAdjacentHTML("afterEnd", "<div class='help-block'>"+infoDiv+"</div>");
        }
    }

    function _removeValidateInfoAfterObject(thisObj) {
        var validateInfoObj = thisObj.nextSibling;
        if($(validateInfoObj).hasClass("help-block")) {
            $(validateInfoObj).remove();
        }
    }

    function validStr(str){
        if(str.indexOf("(") == -1)
            return str;
        return str.substr(0,str.indexOf("("))
    }

    function luhn(value) {
        var length  = value.length,
            mul     = 0,
            prodArr = [[0, 1, 2, 3, 4, 5, 6, 7, 8, 9], [0, 2, 4, 6, 8, 1, 3, 5, 7, 9]],
            sum     = 0;

        while (length--) {
            sum += prodArr[mul][parseInt(value.charAt(length), 10)];
            mul ^= 1;
        }

        return (sum % 10 === 0 && sum > 0);
    }

})(window.jQuery);