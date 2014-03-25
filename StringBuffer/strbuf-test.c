#include <assert.h>
int main(){
	struct strbuf* sb = strbuf_new(2);
	ASSERT(is_strbuf(sb));
	ASSERT(sb->limit == 2 && sb->len == 0);
	char* str = xcalloc(3,sizeof(char));
	str[0] = 'a';
	str[1] = 'b';
	str[2] = '\0';
	strbuf_add(sb,str,3);
	ASSERT(is_strbuf(sb));

	strbuf_addstr(sb,str);
	ASSERT(is_strbuf(sb));

	strbuf_addstr(sb,str);
	ASSERT(is_strbuf(sb));

	ASSERT(b->buf[0] == 'a');
	ASSERT(b->buf[1] == 'b');
	ASSERT(b->buf[2] == 'a');
	ASSERT(b->buf[3] == 'b');
	ASSERT(b->buf[4] == 'a');
	ASSERT(b->buf[5] == 'b');
	ASSERT(b->buf[6] == '\0');

	free(str);
	char* buf = strbuf_str(sb);
	ASSERT(buf[0] == 'a');
	ASSERT(buf[1] == 'b');
	ASSERT(buf[2] == 'a');
	ASSERT(buf[3] == 'b');
	ASSERT(buf[4] == 'a');
	ASSERT(buf[5] == 'b');
	ASSERT(buf[6] == '\0');

	char* a = strbuf_dealloc(sb);
	ASSERT(a != NULL);
	free(buf);
	free(a);
	return 0;
}