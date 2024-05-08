/*
Copyright (c) 2003-2009, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	// 设置宽高
    config.width = 1980;
    config.height = 700;
	config.language = 'zh-cn';
	config.skin = 'office2003';//Kama office2003 v2
	config.removePlugins = 'save,newpage';
	config.toolbar = [ 
			['Source','-','Cut','Copy','Paste','PasteText','PasteFromWord'],
			['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
			['Link','Unlink','-','HorizontalRule','Table','-','Smiley','SpecialChar'],
			'/',
			['Bold','Italic','Underline','Strike'],
			['Subscript','Superscript','-','NumberedList','BulletedList','-','Outdent','Indent'],
			['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
			['TextColor','BGColor'],
			'/',
			['Styles','Format','Font','FontSize'],
			['Image','Flash']
		];
	config.filebrowserBrowseUrl = CKEDITOR.basePath + 'filebrower.faces';
	config.filebrowserWindowWidth = '600';
	config.filebrowserWindowHeight = '400';
};
